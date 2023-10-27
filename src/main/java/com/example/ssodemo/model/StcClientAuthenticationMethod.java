package com.example.ssodemo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "stc_client_authentication_method")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StcClientAuthenticationMethod {
    @Id
    private String id;
    private String value;
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;
}

