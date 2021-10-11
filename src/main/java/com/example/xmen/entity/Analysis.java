package com.example.xmen.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class Analysis {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    private String dna;

    private String result;

}
