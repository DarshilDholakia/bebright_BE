import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public class PostController {

    private PostService postService;

    @PostMapping
    public ResponseEntity<Post> addPost(@RequestBody Post post){
        Post newPost = postService.addPost(post);
        return new ResponseEntity<>(newPost, HttpStatus.CREATED);
    }

    @DeleteMapping(path = "post/{postId}")
    public void deletePostById(@PathVariable("postId") String postId){
        postService.deletePostById(postId);
    }

    @PutMapping(path = "post/{postId}")
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

    @GetMapping(path = "posts/users/{userId}")
    public Post getPostByUser(@PathVariable("userId") String userId){
        return postService.getPostByUser(userId);
    }

    @GetMapping(path = "posts/users/{office}")
    public Post getPostByOffice(@PathVariable("office") String office){
        return postService.getPostByOffice(office);
    }

    @GetMapping(path = "posts/users/{office}/{team}")
    public Post getPostByOffice(@PathVariable("office") String office, @PathVariable("team") String team){
        return postService.getPostByTeam(office, team);
    }

}
