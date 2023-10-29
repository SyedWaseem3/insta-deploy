package com.geekster.InstagramBackendProject.repo;

import com.geekster.InstagramBackendProject.model.AuthenticationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAuthenticationRepo extends JpaRepository<AuthenticationToken, Integer> {
    AuthenticationToken findFirstByTokenValue(String tokenValue);

}
