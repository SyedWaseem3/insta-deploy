package com.geekster.InstagramBackendProject.controller;

import com.geekster.InstagramBackendProject.model.Post;
import com.geekster.InstagramBackendProject.model.User;
import com.geekster.InstagramBackendProject.service.AuthenticationService;
import com.geekster.InstagramBackendProject.service.CommentService;
import com.geekster.InstagramBackendProject.service.LikeService;
import com.geekster.InstagramBackendProject.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    AuthenticationService authenticationService;

    @PostMapping("user/signUp")
    public String userSignUp(@RequestBody @Valid User newUser){
        return userService.userSignUp(newUser);
    }

    //Sign in
    @PostMapping("user/signIn/email/{email}/password/{password}")
    public String userSignIn(@PathVariable String email, @PathVariable String password){
        return userService.userSignIn(email, password);
    }

    //Sign out
    @DeleteMapping("user/signOut")
    public String userSignOut(@RequestParam String email,@RequestParam String tokenValue){
        return userService.userSignOut(email, tokenValue);
    }

    @PostMapping("InstaPost")
    public String createInstaPost(@RequestParam String email, @RequestParam String tokenValue, @RequestBody Post instaPost){
        return userService.createInstaPost(email, tokenValue, instaPost);
    }

    @DeleteMapping("InstaPost/{postId}")
    public String deletePostById(@RequestParam String email,@RequestParam String tokenValue,@PathVariable Integer postId){
        return userService.deletePostById(email,tokenValue,postId);
    }

    //Add a like
    @PostMapping("like/post/{postId}")
    public String likePostById(@RequestParam String email,@RequestParam String tokenValue,@PathVariable Integer postId){
        return userService.addLike(email, tokenValue, postId);
    }

    //Remove a like
    @DeleteMapping("unlike/post/{postId}")
    public String removeLike(@RequestParam String email,@RequestParam String tokenValue,@PathVariable Integer postId){
        return userService.removeLike(email, tokenValue, postId);
    }

    //Get likes of a post id
    @GetMapping("count/likes/post/{postId}")
    public String getLikesByPostId(@RequestParam String email,@RequestParam String tokenValue,@PathVariable Integer postId){
        return userService.getLikesByPostId(email,tokenValue,postId);
    }


    //Add comment
    @PostMapping("comment/post/{postId}")
    public String addComment(@RequestParam String email,@RequestParam String tokenValue,@PathVariable Integer postId, @RequestBody String commentBody){
        return userService.addComment(email, tokenValue, postId, commentBody);
    }


    //Delete a comment
    @DeleteMapping("comment/{commentId}")
    public String removeComment(@RequestParam String email,@RequestParam String tokenValue,@PathVariable Integer commentId){
        return userService.removeComment(email, tokenValue, commentId);
    }


    //Follow
    @PostMapping("follow/user/{targetUserId}")
    public String followTarget(@RequestParam String email,@RequestParam String tokenValue,@PathVariable Integer targetUserId){
        return userService.followTarget(email, tokenValue, targetUserId);
    }


    //Unfollow


    //Get followers of a user


    //Get following of a user
}
