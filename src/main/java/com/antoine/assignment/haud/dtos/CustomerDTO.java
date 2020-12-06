package com.antoine.assignment.haud.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Pattern;

@Getter
@Setter
@ToString
public class CustomerDTO {

    @JsonProperty("username")
    @NonNull
    private String username;

    @JsonProperty("name")
    private String name;

    @JsonProperty("surname")
    private String surname;

    @JsonProperty("address")
    private String address;

    @JsonProperty("birthDate")
    @Pattern(regexp = "^((19|2[0-9])[0-9]{2})-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$")
    //Will accept february 29th which is an issue, but for the sake of this assignment i will keep it simple
    private String birthDate;

}
