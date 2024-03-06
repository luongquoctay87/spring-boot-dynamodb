package com.sample.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@DynamoDBDocument
@JsonIgnoreProperties(ignoreUnknown = true)
public class Address implements Serializable {
    @DynamoDBAttribute
    private String street;

    @DynamoDBAttribute
    private String district;

    @DynamoDBAttribute
    private String city;

    @DynamoDBAttribute
    private String country;

    @DynamoDBAttribute
    private String postalCode;

    @DynamoDBAttribute
    private String text;
}
