package com.sample.service;

import com.sample.dto.request.UserCreationRequest;
import com.sample.dto.request.UserUpdateRequest;
import com.sample.dto.response.LoadingPageResponse;
import com.sample.dto.response.UserResponse;

public interface UserService {

    String saveUser(UserCreationRequest request);

    String updateUser(UserUpdateRequest request) ;

    String deactivateUser(String hashKey, String rangeKey) ;

    String deleteUser(String hashKey, String rangeKey);

    UserResponse getUser(String hashKey, String rangeKey);
    LoadingPageResponse getAllUsers(String hashKey, String search, String firstName, String lastName, String phone, String email, String address, String status, String orderBy, String nextKey, int pageSize);
}
