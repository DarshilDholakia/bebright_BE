package com.hackathon.bebright.posts;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping(path = "posts")
    public ResponseEntity<Post> addPost(@RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken,
                                        @RequestBody Post post){
        Post newPost = postService.addPost(bearerToken, post);
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

    @GetMapping(path = "posts")
    public List<Post> getPostsByUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken){
        return postService.getPostsByUser(bearerToken);
    }

    @GetMapping(path = "posts/office/{office}")
    public List<Post> getPostsByOffice(@RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken,
                                             @PathVariable("office") String office){
        return postService.getPostsByOffice(bearerToken, office);
    }

    @GetMapping(path = "posts/offices")
    public List<Post> getPostsByMultipleOffice(@RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken) {
        return postService.getPostsByMultipleOffice(bearerToken);
    }

    @GetMapping(path = "posts/{office}/{team}")
    public List<Post> getPostsByOfficeAndTeam(@PathVariable("office") String office, @PathVariable("team") String team){
        return postService.getPostsByOfficeAndTeam(office, team);
    }

    @PutMapping(path = "posts/addLike/{postId}")
    public Post addLike(@PathVariable("postId") String postId) {
        return postService.addLike(postId);
    }

    @PutMapping(path = "posts/removeLike/{postId}")
    public Post removeLike(@PathVariable("postId") String postId) {
        return postService.removeLike(postId);
    }

    @GetMapping(path = "posts/check/{postId}")
    public boolean checkIfPostBelongsToCurrentUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken,
                                                   @PathVariable("postId") String postId) {
        return postService.checkIfPostBelongsToCurrentUser(bearerToken, postId);
    }

    @GetMapping(path = "posts/check")
    public boolean checkIfCurrentUserHasPosted(@RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken) {
        return postService.checkIfCurrentUserHasPosted(bearerToken);
    }

}
