package com.hana.cheers_up.application.pub.domain;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Entity
public class Pub {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String pubName;
    private String pubAddress;
    private double latitude;
    private double longitude;
    private String categoryName;

    protected Pub pub(){
        return new Pub();
    }
}
