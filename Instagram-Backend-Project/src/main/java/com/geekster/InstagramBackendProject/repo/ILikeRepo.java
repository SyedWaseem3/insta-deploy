package com.geekster.InstagramBackendProject.repo;

import com.geekster.InstagramBackendProject.model.Like;
import com.geekster.InstagramBackendProject.model.Post;
import com.geekster.InstagramBackendProject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ILikeRepo extends JpaRepository<Like, Integer> {
    List<Like> findByInstaPost(Post myPost);

    List<Like> findByInstaPostAndLiker(Post instaPostToBeLiked, User liker);

}
