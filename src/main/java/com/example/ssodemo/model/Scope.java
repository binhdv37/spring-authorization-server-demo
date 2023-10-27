package com.example.ssodemo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "scope")
@Getter
@Setter
public class Scope {
    @Id
    private String id;
    private String value;
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;
}
