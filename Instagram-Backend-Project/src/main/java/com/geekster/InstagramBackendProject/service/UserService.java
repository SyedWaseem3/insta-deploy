package com.geekster.InstagramBackendProject.service;

import com.geekster.InstagramBackendProject.model.*;
import com.geekster.InstagramBackendProject.repo.IUserRepo;
import com.geekster.InstagramBackendProject.service.emailUtility.EmailHandler;
import com.geekster.InstagramBackendProject.service.hashingUtility.PasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

@Service
public class UserService {
    @Autowired
    IUserRepo userRepo;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    PostService postService;

    @Autowired
    LikeService likeService;

    @Autowired
    CommentService commentService;

    @Autowired
    FollowService followService;

    public String userSignUp(User newUser) {
        //check if already exists -> not allowed : try logging in

        String newEmail = newUser.getUserEmail();

        User existingUser = userRepo.findFirstByUserEmail(newEmail);

        if(existingUser != null){
            return "email already exists!";
        }
        // passwords are encrypted before we store it in table
        String signUpPassword = newUser.getUserPassword();

        try {
            String encryptedPassword = PasswordEncryptor.encrypt(signUpPassword);
            newUser.setUserPassword(encryptedPassword);
            userRepo.save(newUser);
            return "Insta User registered!";
        } catch (NoSuchAlgorithmException e) {
            return "Internal Server issues while saving password, try again later!!!";
        }

        //patient table - save patient
    }

    public String userSignIn(String email, String password) {
        //check if the email is there in table
        //sign in only possible if this person ever signed up

        User existingUser = userRepo.findFirstByUserEmail(email);

        if(existingUser == null){
            return "Not a valid email, Please sign up first";
        }

        //password should be matched

        try {
            String encryptedPassword = PasswordEncryptor.encrypt(password);

            if(existingUser.getUserPassword().equals(encryptedPassword)){
                //return a token for this sign in
                AuthenticationToken token = new AuthenticationToken(existingUser);

                if(EmailHandler.sendEmail(email,"otp after login", token.getTokenValue())){
                    authenticationService.createToken(token);
                }else{
                    return "error while generating token!";
                }


                return "check email for otp/token!";
            }else{
                //password is wrong
                return "Invalid Credentials!";
            }
        } catch (NoSuchAlgorithmException e) {
            return "Internal Server issues while saving password, try again later!!!";
        }

    }

    public String userSignOut(String email, String tokenValue) {
        if(authenticationService.authenticate(email, tokenValue)){
            authenticationService.deleteToken(tokenValue);
            return "Sign Out successful!";
        }else{
            return "Un Authenticated access!";
        }
    }

    public String createInstaPost(String email, String tokenValue, Post instaPost) {

        if(authenticationService.authenticate(email, tokenValue)){

            User existingUser = userRepo.findFirstByUserEmail(email);
            instaPost.setPostOwner(existingUser);

            postService.createInstaPost(instaPost);
            return instaPost.getPostType() + " posted!!";

        }else{
            return "Un Authenticated access!";
        }
    }

    public String deletePostById(String email, String tokenValue, Integer postId) {

        if(authenticationService.authenticate(email, tokenValue)){

            Post instaPost = postService.getPostById(postId);
            String postOwnerEmail = instaPost.getPostOwner().getUserEmail();

            if(email.equals(postOwnerEmail)){
                postService.deletePost(postId);
                return "post removed!";
            }else{
                return "Un Authorized access!";
            }

        }else{
            return "Un Authenticated access!";
        }
    }

    public String addLike(String email, String tokenValue, Integer postId) {
        if(authenticationService.authenticate(email, tokenValue)){

            //figure out the post which we are liking
            Post instaPostToBeLiked = postService.getPostById(postId);

            //we have to figure out the liker
            User liker = userRepo.findFirstByUserEmail(email);

            //user cannot like this particular postId more than once

            boolean alreadyLiked = likeService.isAlreadyLiked(instaPostToBeLiked, liker);

            if(!alreadyLiked) {
                Like newLike = new Like(null, instaPostToBeLiked, liker);

                likeService.addLike(newLike);

                return liker.getUserHandle() + " liked " +postId;
            }else{
                return "already liked!!";
            }
        }else{
            return "Un Authenticated access!";
        }
    }

    public String removeLike(String email, String tokenValue, Integer postId) {
        if(authenticationService.authenticate(email, tokenValue)){

            User potentialLiker = userRepo.findFirstByUserEmail(email);

            Post instaPostToBeUnLiked = postService.getPostById(postId);

            return likeService.removeLikesByLikerAndPost(potentialLiker, instaPostToBeUnLiked);

        }else{
            return "Un Authenticated access!";
        }
    }

    public String getLikesByPostId(String email, String tokenValue, Integer postId) {
        if(authenticationService.authenticate(email, tokenValue)){

            Post instaPost = postService.getPostById(postId);

            return likeService.getLikesForPost(instaPost);

        }else{
            return "Un Authenticated access!";
        }
    }

    public String addComment(String email, String tokenValue, Integer postId, String commentBody) {
        if(authenticationService.authenticate(email, tokenValue)){

            //figure out the post which we are commenting
            Post instaPostToBeCommented = postService.getPostById(postId);

            //we have to figure out the commenter
            User commentor = userRepo.findFirstByUserEmail(email);

            //functionally more than 1 comment can be made on particular post

            Comment newComment = new Comment(null, commentBody, LocalDateTime.now(), instaPostToBeCommented, commentor);

            commentService.addComment(newComment);

            return commentor.getUserHandle() + " commented " +postId;

        }else{
            return "Un Authenticated access!";
        }
    }

    public String removeComment(String email, String tokenValue, Integer commentId) {
        if(authenticationService.authenticate(email, tokenValue)){

            Comment comment = commentService.findCommentById(commentId);

            Post instaPostOfComment = comment.getInstaPost();

            if(authorizeCommentRemover(email, instaPostOfComment, comment)){

                commentService.removeCommentById(commentId);

            }else{
                return "Not authorized!!";
            }

            return "Comment deleted!";

        }else{
            return "Un authorized access!!";
        }
    }

    private boolean authorizeCommentRemover(String email, Post instaPostOfComment, Comment comment) {

        User potentialRemover = userRepo.findFirstByUserEmail(email);

        //only the commenter and owner of the post should be allowed to delete the comment

        return potentialRemover.equals(instaPostOfComment.getPostOwner()) || potentialRemover.equals(comment.getCommenter());

    }

    public String followTarget(String email, String tokenValue, Integer targetUserId) {

        if(authenticationService.authenticate(email, tokenValue)){

            User follower = userRepo.findFirstByUserEmail(email);

            User target = userRepo.findById(targetUserId).orElseThrow();

            if(authorizeToFollow(follower, target)){

                followService.startFollowing(follower, target);
                return follower.getUserHandle()+ " started following " +target.getUserHandle();

            }else{
                return "Already follows, cannot re-follow";
            }

        }else{
            return "Un authorized access!!";
        }
    }

    private boolean authorizeToFollow(User follower, User target) {

        //check if already follows or not

        boolean followingAlreadyExists = followService.findByTargetAndFollower(follower, target);

        return !followingAlreadyExists && !follower.equals(target);
    }
}
