package com.sample.service.impl;

import com.sample.dto.request.UserCreationRequest;
import com.sample.dto.request.UserUpdateRequest;
import com.sample.dto.response.LoadingPageResponse;
import com.sample.dto.response.UserResponse;
import com.sample.exception.InvalidDataException;
import com.sample.exception.ResourceNotFoundException;
import com.sample.model.User;
import com.sample.repository.UserRepository;
import com.sample.service.UserService;
import com.sample.util.UserStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;

    /**
     * Add new user
     *
     * @param request
     * @return
     */
    @Override
    public String saveUser(UserCreationRequest request) {
        log.info("Saving user ...");

        isPhoneValid(request.getPhone());
        isEmailValid(request.getEmail());

        String searchKeys = String.format("%s %s, %s, %s, %s", request.getFirstName(), request.getLastName(), request.getPhone(), request.getEmail(), request.getAddress());
        User object = User.builder()
                .hashKey("uuid")
                .rangeKey(UUID.randomUUID().toString())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .password(request.getPassword())
                .address(request.getAddress())
                .status(UserStatus.NONE)
                .isFirstLogin(false)
                .searchKeys(searchKeys)
                .build();

        User response = userRepository.save(object);

        return response.getRangeKey();
    }

    /**
     * Update user by ID
     *
     * @param request
     * @return message
     */
    @Override
    public String updateUser(UserUpdateRequest request) {
        log.info("Updating user ...");

        User user = userRepository.findByCompositeKey(request.getHashKey(), request.getRangeKey());
        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }

        if (StringUtils.hasLength(request.getPhone()) && !user.getPhone().equals(request.getPhone())) {
            isPhoneValid(request.getPhone());
        }

        if (StringUtils.hasLength(request.getEmail()) && !user.getEmail().equals(request.getEmail())) {
            isEmailValid(request.getEmail());
        }

        String searchKeys = String.format("%s %s, %s, %s, %s", request.getFirstName(), request.getLastName(), request.getPhone(), request.getEmail(), request.getAddress());
        if (StringUtils.hasLength(request.getFirstName())) {
            user.setFirstName(request.getFirstName());
        }

        if (StringUtils.hasLength(request.getLastName())) {
            user.setLastName(request.getLastName());
        }

        if (StringUtils.hasLength(request.getPhone())) {
            user.setPhone(request.getPhone());
        }

        if (StringUtils.hasLength(request.getEmail())) {
            user.setEmail(request.getEmail());
        }

        if (!ObjectUtils.isEmpty(request.getAddress())) {
            user.setAddress(request.getAddress());
        }

        user.setSearchKeys(searchKeys);

        userRepository.save(user);
        return "updated";
    }

    /**
     * Deactivate user
     *
     * @param hashKey
     * @param rangeKey
     * @return message
     */
    @Override
    public String deactivateUser(String hashKey, String rangeKey) {
        log.info("Deactivating user ...");

        User user = userRepository.findByCompositeKey(hashKey, rangeKey);
        if (user == null) throw new ResourceNotFoundException("User not found");
        user.setStatus(UserStatus.INACTIVE);
        userRepository.save(user);
        return "deactivated!";
    }

    /**
     * Delete permanent user
     *
     * @param hashKey
     * @param rangeKey
     * @return message
     */
    @Override
    public String deleteUser(String hashKey, String rangeKey) {
        log.info("Deleting user ...");
        userRepository.delete(hashKey, rangeKey);
        return "deleted!";
    }

    /**
     * Get user by composite key
     *
     * @param hashKey
     * @param rangeKey
     * @return user
     */
    @Override
    public UserResponse getUser(String hashKey, String rangeKey) {
        log.info("Getting user ...");

        User user = userRepository.findByCompositeKey(hashKey, rangeKey);
        if (user == null) throw new ResourceNotFoundException("User not found");
        return UserResponse.builder()
                .id(user.getRangeKey())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .email(user.getEmail())
                .address(user.getAddress())
                .build();
    }

    /**
     * Get user list
     *
     * @param hashKey
     * @param search
     * @param firstName
     * @param lastName
     * @param phone
     * @param email
     * @param address
     * @param status
     * @param nextKey
     * @param pageSize
     * @return list of users
     */
    @Override
    public LoadingPageResponse getAllUsers(String hashKey, String search, String firstName, String lastName, String phone, String email, String address, String status, String orderBy, String nextKey, int pageSize) {
        log.info("Getting list of user ...");

        return userRepository.findAll(hashKey, search, firstName, lastName, phone, email, address, status, orderBy, nextKey, pageSize);
    }

    /**
     * Check phone exists or not
     *
     * @param phone
     */
    private void isPhoneValid(String phone) {
        log.info("Validating phone number={}", phone);

        if (!phone.matches("^(\\+\\d{1,2}\\s?)?1?\\-?\\.?\\s?\\(?\\d{3}\\)?[\\s.-]?\\d{3}[\\s.-]?\\d{4}$"))
            throw new InvalidDataException("Phone number invalid format");

        User object = userRepository.findByPhone(phone);
        if (object != null) {
            log.warn("Phone number={} exists", phone);
            throw new InvalidDataException("Phone is registered");
        }
    }

    /**
     * Check phone exists or not
     *
     * @param email
     */
    private void isEmailValid(String email) {
        log.info("Validating email address={}", email);

        User object = userRepository.findByEmail(email);
        if (object != null) {
            log.warn("Email address={} exists", email);
            throw new InvalidDataException("Email is registered");
        }
    }
}
