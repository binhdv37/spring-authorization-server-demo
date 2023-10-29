package com.example.ssodemo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "scope")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Scope {
    @Id
    private String id;
    private String scope; // ScopeConst
}
