package com.sample.dto.request;

import com.sample.model.Address;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UserCreationRequest implements Serializable {

    @NotBlank(message = "rangeKey must be not blank")
    private String rangeKey;

    @NotBlank(message = "firstName must be not blank")
    private String firstName;

    @NotBlank(message = "lastName must be not blank")
    private String lastName;

    @NotBlank(message = "phone must be not blank")
    private String phone;

    @NotBlank(message = "email must be not blank")
    private String email;

    @NotBlank(message = "password must be not blank")
    private String password;

    private Address address;
}
