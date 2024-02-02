package com.example.daocradapi.models.abstractclases;

import com.example.daocradapi.models.abstractclases.Thing;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "things")
public abstract class ManThins extends Thing {
}
