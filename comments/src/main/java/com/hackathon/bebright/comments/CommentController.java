package com.hackathon.bebright.comments;

import com.hackathon.bebright.clients.comments.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping(path = "comments")
    public ResponseEntity<Comment> addComment(@RequestBody Comment comment, @RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken){
        Comment newComment = commentService.addComment(comment, bearerToken);
        return new ResponseEntity<>(newComment, HttpStatus.CREATED);
    }

    @DeleteMapping(path = "comments/{commentId}")
    public void deleteCommentById(@PathVariable("commentId") String commentId){
        commentService.deleteCommentById(commentId);
    }

    @PutMapping(path = "comments/{commentId}")
    public Comment updateCommentById(@PathVariable("commentId") String commentId, @RequestBody Comment updatedComment){
        return commentService.updateCommentById(commentId, updatedComment);
    }

    @GetMapping(path = "comments/all")
    public List<Comment> getAllComments(){
        return commentService.getAllComments();
    }

    @GetMapping(path = "comments/{commentId}")
    public Comment getCommentById(@PathVariable("commentId") String commentId){
        return commentService.getCommentById(commentId);
    }

    @GetMapping(path = "comments/post/{postId}")
    public List<Comment> getCommentsByPostId(@PathVariable("postId") String postId){
        return commentService.getCommentsByPostId(postId);
    }

}
