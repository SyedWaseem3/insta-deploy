package com.geekster.InstagramBackendProject.repo;

import com.geekster.InstagramBackendProject.model.Follow;
import com.geekster.InstagramBackendProject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IFollowRepo extends JpaRepository<Follow, Integer> {
    List<Follow> findByCurrentUserAndCurrentUserFollower(User target, User follower);

}
