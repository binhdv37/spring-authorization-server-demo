package com.example.ssodemo.repo;

import com.example.ssodemo.model.StcClientAuthenticationMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StcClientAuthenticationMethodRepository extends JpaRepository<StcClientAuthenticationMethod, String> {

    List<StcClientAuthenticationMethod> findAllByMethodIn(List<String> methods);

}
