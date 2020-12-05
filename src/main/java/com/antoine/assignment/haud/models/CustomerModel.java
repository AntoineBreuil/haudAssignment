package com.antoine.assignment.haud.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class CustomerModel {

    private String username;

    private String name;

    private String surname;

    private String address;

    private LocalDate birthDate;

}
