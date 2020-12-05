package com.antoine.assignment.haud.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SimDTO {

    @JsonProperty("iccid")
    @NonNull
    private String ICCID;

    @JsonProperty("ki")
    private String KI;

    @JsonProperty("pin")
    private String PIN;

    @JsonProperty("puk")
    private String PUK;

    @JsonProperty("imsi")
    private String IMSI;
}
