package com.geekster.InstagramBackendProject.service;

import com.geekster.InstagramBackendProject.model.Comment;
import com.geekster.InstagramBackendProject.model.Post;
import com.geekster.InstagramBackendProject.repo.ICommentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    @Autowired
    ICommentRepo commentRepo;
    public void clearCommentsByPost(Post myPost) {
        List<Comment> commentsOfPost = commentRepo.findByInstaPost(myPost);
        commentRepo.deleteAll(commentsOfPost);
    }

    public void addComment(Comment newComment) {
        commentRepo.save(newComment);
    }

    public Comment findCommentById(Integer commentId) {
        return commentRepo.findById(commentId).orElseThrow();
    }

    public void removeCommentById(Integer commentId) {
        commentRepo.deleteById(commentId);
    }
}
