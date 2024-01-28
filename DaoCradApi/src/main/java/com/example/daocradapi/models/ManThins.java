package com.example.daocradapi.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "things")
public abstract class ManThins extends Thing {
}
