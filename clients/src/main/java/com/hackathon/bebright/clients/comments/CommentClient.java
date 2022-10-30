package com.hackathon.bebright.clients.comments;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient("comments")
public interface CommentClient {

    @GetMapping(path = "comments/post/{postId}")
    List<Comment> getCommentsByPostId(@PathVariable("postId") String postId);

}
