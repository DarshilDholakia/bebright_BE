package com.hackathon.bebright.comments;

import com.hackathon.bebright.comments.exceptions.CommentNotFoundException;
import com.hackathon.bebright.comments.exceptions.InvalidRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public Comment addComment(Comment comment) {
        checkCommentInputProperties(comment);
        Comment addComment = new Comment(comment.getPostId(), comment.getUsername(), comment.getCommentText());
        return commentRepository.insert(addComment);
    }

    public void deleteCommentById(String commentId) {
        Comment existingComment = getCommentOrThrowNull(commentId); // Check comment exists first
        commentRepository.delete(existingComment);
    }

    public Comment updateCommentById(String commentId, Comment updatedComment) {
        Comment existingComment = commentRepository.findById(commentId).get();
        existingComment.setCommentText(updatedComment.getCommentText());
        return commentRepository.save(existingComment);
    }

    public List<Comment> getAllComments() {
        List<Comment> commentList = commentRepository.findAll();
        if (commentList == null){
            throw new InvalidRequestException("There were no comments found");
        }
        return commentList;
    }

    public Comment getCommentById(String commentId) {
        return getCommentOrThrowNull(commentId);
    }

    public List<Comment> getCommentsByPostId(String postId) {
        return commentRepository.findByPostId(postId);
    }

    private Comment getCommentOrThrowNull(String commentId){
        if (commentId == null || commentId.isEmpty()){
            throw new CommentNotFoundException("Comment id is invalid");
        }
        return commentRepository.findById(commentId).orElseThrow();
    }

    private void checkCommentInputProperties(Comment comment) {
        if (comment.getPostId() == null) {
            throw new InvalidRequestException("Post id cannot be null");
        }
        if (comment.getUsername() == null) {
            throw new InvalidRequestException("Username cannot be null");
        }
        if (comment.getCommentText() == null) {
            throw new InvalidRequestException("Comment text cannot be null");
        }
    }
}
