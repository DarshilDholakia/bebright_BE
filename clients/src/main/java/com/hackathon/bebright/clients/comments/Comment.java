package com.hackathon.bebright.clients.comments;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data // does getters and setters, toString etc for you
@AllArgsConstructor // creates a constructor with all arguments
@NoArgsConstructor
@Document(collection = "comment") // enables us to specify that this class will be a collection (tables equivalent in mongoDb)
public class Comment {

    @Id
    private String commentId;
    private String postId;
    private String username;
    private String commentText;
    private LocalDateTime createdAt;

    public Comment(String postId, String username, String commentText) {
        this.postId = postId;
        this.username = username;
        this.commentText = commentText;
//        DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");
        this.createdAt = LocalDateTime.now();
    }

    public Comment(String username, String commentText, LocalDateTime createdAt) {
        this.username = username;
        this.commentText = commentText;
//        this.createdAt = createdAt;
    }
}
