package com.example.daocradapi.models.products;

import com.example.daocradapi.models.abstractclases.WomanThings;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "things")
public class WomanSuit extends WomanThings {
}
