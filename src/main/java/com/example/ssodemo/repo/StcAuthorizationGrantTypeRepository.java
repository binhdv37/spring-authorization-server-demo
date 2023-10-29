package com.example.ssodemo.repo;

import com.example.ssodemo.model.StcAuthorizationGrantType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StcAuthorizationGrantTypeRepository extends JpaRepository<StcAuthorizationGrantType, String> {

    List<StcAuthorizationGrantType> findAllByGrantTypeIn(List<String> grantTypes);
}
