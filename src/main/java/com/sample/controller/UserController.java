package com.sample.controller;


import com.sample.dto.request.UserCreationRequest;
import com.sample.dto.request.UserUpdateRequest;
import com.sample.dto.response.LoadingPageResponse;
import com.sample.dto.response.UserResponse;
import com.sample.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.sample.util.Constant.apiKey;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Validated
@Slf4j
@Tag(name = "User Controller")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Add new user", description = "Return user ID")
    @PostMapping(path = "/add", headers = apiKey, produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    public String createUser(@Validated @RequestBody UserCreationRequest request) {
        return userService.saveUser(request);
    }

    @Operation(summary = "Update user", description = "Return message")
    @PutMapping(path = "/user/{hashKey}", headers = apiKey, produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(ACCEPTED)
    public String updateUser(@Valid @RequestBody UserUpdateRequest request) {
        return userService.updateUser(request);
    }

    @Operation(summary = "Deactivate user", description = "Return message")
    @PatchMapping(path = "/user/{hashKey}", headers = apiKey, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(ACCEPTED)
    public String deactivateUser(@PathVariable String hashKey, @RequestParam String rangeKey) {
        return userService.deactivateUser(hashKey, rangeKey);
    }

    @Operation(summary = "Delete user", description = "Return message")
    @DeleteMapping(path = "/user/{hashKey}", headers = apiKey, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(NO_CONTENT)
    public String deleteUser(@PathVariable String hashKey, @RequestParam String rangeKey) {
        return userService.deleteUser(hashKey, rangeKey);
    }

    @Operation(summary = "Get user detail", description = "Return user detail")
    @GetMapping(path = "/user/{hashKey}", headers = apiKey, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    public UserResponse getUserDetail(@PathVariable String hashKey, @RequestParam String rangeKey) {
        return userService.getUser(hashKey, rangeKey);
    }

    @Operation(summary = "Get all of users", description = "Return user lists")
    @GetMapping(path = "/users", headers = apiKey, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    public LoadingPageResponse getUserList(@RequestParam String hashKey,
                                       @RequestParam(required = false) String search,
                                       @RequestParam(required = false) String firstName,
                                       @RequestParam(required = false) String lastName,
                                       @RequestParam(required = false) String phone,
                                       @RequestParam(required = false) String email,
                                       @RequestParam(required = false) String address,
                                       @RequestParam(required = false) String status,
                                       @RequestParam(required = false) String orderBy,
                                       @RequestParam(required = false) String nextKey,
                                       @RequestParam(defaultValue = "25") int pageSize) {
        return userService.getAllUsers(hashKey, search, firstName, lastName, phone, email, address, status, orderBy, nextKey, pageSize);
    }
}
