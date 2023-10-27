package com.example.ssodemo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "post_logout_redirect_uri")
@Getter
@Setter
public class PostLogoutRedirectUri {
    @Id
    private String id;
    private String value;
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;
}
