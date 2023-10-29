package com.geekster.InstagramBackendProject.service;

import com.geekster.InstagramBackendProject.model.Like;
import com.geekster.InstagramBackendProject.model.Post;
import com.geekster.InstagramBackendProject.model.User;
import com.geekster.InstagramBackendProject.repo.ILikeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LikeService {
    @Autowired
    ILikeRepo likeRepo;
    public void clearLikesByPost(Post myPost) {
        List<Like> likesOfPost = likeRepo.findByInstaPost(myPost);
        likeRepo.deleteAll(likesOfPost);
    }

    public void addLike(Like newLike) {
        likeRepo.save(newLike);
    }

    public boolean isAlreadyLiked(Post instaPostToBeLiked, User liker) {
        List<Like> likes = likeRepo.findByInstaPostAndLiker(instaPostToBeLiked, liker);

        return (likes != null && likes.size() != 0);
    }

    public String removeLikesByLikerAndPost(User potentialLiker, Post instaPostToBeUnLiked) {
        List<Like> likes = likeRepo.findByInstaPostAndLiker(instaPostToBeUnLiked, potentialLiker);

        if(!likes.isEmpty()){
            likeRepo.deleteAll(likes);
            return "Un-liked!";
        }else{
            return "Not liked yet, cannot dislike!";
        }
    }

    public String getLikesForPost(Post instaPost) {
        List<Like> likes = likeRepo.findByInstaPost(instaPost);

        return String.valueOf(likes.size());
    }
}
