package com.example.ssodemo.repo;

import com.example.ssodemo.model.RedirectUri;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedirectUriRepository extends JpaRepository<RedirectUri, Long> {
}
