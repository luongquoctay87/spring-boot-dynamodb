package com.sample.dto.response;

import com.sample.model.Address;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Builder
public class UserResponse implements Serializable {
    private String id;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private Address address;
}
