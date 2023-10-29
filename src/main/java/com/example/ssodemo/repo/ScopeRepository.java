package com.example.ssodemo.repo;

import com.example.ssodemo.model.Scope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScopeRepository extends JpaRepository<Scope, String> {
    List<Scope> findAllByScopeIn(List<String> scopes);
}
