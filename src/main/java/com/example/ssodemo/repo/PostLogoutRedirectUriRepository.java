package com.example.ssodemo.repo;

import com.example.ssodemo.model.PostLogoutRedirectUri;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostLogoutRedirectUriRepository extends JpaRepository<PostLogoutRedirectUri, String> {
}
