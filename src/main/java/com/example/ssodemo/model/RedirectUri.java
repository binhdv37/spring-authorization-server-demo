package com.example.ssodemo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "redirect_uri")
public class RedirectUri {
    @Id
    private String id;
    private String value;
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;
}
