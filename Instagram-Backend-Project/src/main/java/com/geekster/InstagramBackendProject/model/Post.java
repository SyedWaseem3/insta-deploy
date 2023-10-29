package com.geekster.InstagramBackendProject.model;

import com.geekster.InstagramBackendProject.model.enums.PostType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer postId;
    private String postContent;
    private String postCaption;
    private String postLocation;
    private PostType postType;

    private LocalDateTime postCreationTimeStamp;

    @ManyToOne
    @JoinColumn(name = "fk_owner_user_id")
    private User postOwner;
}
