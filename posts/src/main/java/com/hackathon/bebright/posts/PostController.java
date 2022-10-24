package com.hackathon.bebright.posts;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping(path = "posts")
    public ResponseEntity<Post> addPost(@RequestBody Post post){
        Post newPost = postService.addPost(post);
        return new ResponseEntity<>(newPost, HttpStatus.CREATED);
    }

    @DeleteMapping(path = "posts/{postId}")
    public void deletePostById(@PathVariable("postId") String postId){
        postService.deletePostById(postId);
    }

    @PutMapping(path = "posts/{postId}")
    public Post updatePostById(@PathVariable("postId") String postId, @RequestBody Post updatedPost){
        return postService.updatePostById(postId, updatedPost);
    }

    @GetMapping(path = "posts/all")
    public List<Post> getAllPosts(){
        return postService.getAllPosts();
    }

    @GetMapping(path = "posts/{postId}")
    public Post getPostById(@PathVariable("postId") String postId){
        return postService.getPostById(postId);
    }

    @GetMapping(path = "posts/{userId}")
    public List<Post> getPostsByUser(@PathVariable("userId") String userId){
        return postService.getPostsByUser(userId);
    }

    @GetMapping(path = "posts/users/{office}")
    public List<List<Post>> getPostsByOffice(@PathVariable("office") String office){
        return postService.getPostsByOffice(office);
    }

    @GetMapping(path = "posts/users/{office}/{team}")
    public List<List<Post>> getPostsByOffice(@PathVariable("office") String office, @PathVariable("team") String team){
        return postService.getPostsByTeam(office, team);
    }

}
