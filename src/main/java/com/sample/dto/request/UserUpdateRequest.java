package com.sample.dto.request;

import com.sample.model.Address;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UserUpdateRequest implements Serializable {
    @NotBlank(message = "hashKey must be not blank")
    private String hashKey;

    @NotBlank(message = "hashKey must be not blank")
    private String rangeKey;

    private String firstName;

    private String lastName;

    private String phone;

    private String email;

    private Address address;
}
