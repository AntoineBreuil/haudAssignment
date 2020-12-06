package com.antoine.assignment.haud.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name= "sims")
@ToString
public class Sim {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, name = "iccId")
    private String ICCID;

    @Column(name = "ki")
    private String KI;

    @Column(name = "pin")
    private String PIN;

    @Column(name = "puk")
    private String PUK;

    @Column(name = "imsi")
    private String IMSI;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
}
