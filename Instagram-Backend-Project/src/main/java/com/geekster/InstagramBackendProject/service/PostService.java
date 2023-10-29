package com.geekster.InstagramBackendProject.service;

import com.geekster.InstagramBackendProject.model.Post;
import com.geekster.InstagramBackendProject.repo.IPostRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PostService {
    @Autowired
    IPostRepo postRepo;

    @Autowired
    LikeService likeService;

    @Autowired
    CommentService commentService;
    public void createInstaPost(Post instaPost) {

        //set creation time before saving :

        instaPost.setPostCreationTimeStamp(LocalDateTime.now());
        postRepo.save(instaPost);
    }

    public Post getPostById(Integer postId) {
        return postRepo.findById(postId).orElseThrow();
    }

    @Transactional
    public void deletePost(Integer postId) {
        Post myPost = postRepo.findById(postId).orElseThrow();

        //delete all likes
        likeService.clearLikesByPost(myPost);

        //delete all comments
        commentService.clearCommentsByPost(myPost);

        //finally delete post
        postRepo.deleteById(postId);
    }
}
