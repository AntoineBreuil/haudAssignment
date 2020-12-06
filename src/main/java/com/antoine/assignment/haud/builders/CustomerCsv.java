package com.antoine.assignment.haud.builders;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class CustomerCsv {

    private long id;

    private String username;

    private String name;

    private String surname;

    private String address;

    private LocalDate birthDate;

    private String sims;

}

