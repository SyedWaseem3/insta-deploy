package com.geekster.InstagramBackendProject.repo;

import com.geekster.InstagramBackendProject.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPostRepo extends JpaRepository<Post, Integer> {
}
