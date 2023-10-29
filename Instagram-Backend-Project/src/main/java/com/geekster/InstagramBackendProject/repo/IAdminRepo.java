package com.geekster.InstagramBackendProject.repo;

import com.geekster.InstagramBackendProject.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAdminRepo extends JpaRepository<Admin, Integer> {
}
