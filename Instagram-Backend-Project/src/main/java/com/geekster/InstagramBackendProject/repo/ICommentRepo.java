package com.geekster.InstagramBackendProject.repo;

import com.geekster.InstagramBackendProject.model.Comment;
import com.geekster.InstagramBackendProject.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ICommentRepo extends JpaRepository<Comment, Integer> {
    List<Comment> findByInstaPost(Post myPost);

}
