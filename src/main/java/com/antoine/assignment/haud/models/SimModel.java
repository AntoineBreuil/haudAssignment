package com.antoine.assignment.haud.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SimModel {

    private String ICCID;
    private String KI;
    private String PIN;
    private String PUK;
    private String IMSI;
}
