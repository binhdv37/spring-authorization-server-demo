package com.example.ssodemo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "stc_authorization_grant_type")
@Getter
@Setter
public class StcAuthorizationGrantType {
    @Id
    private String id;
    private String value;
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;
}
