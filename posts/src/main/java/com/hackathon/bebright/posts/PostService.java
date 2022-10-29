package com.hackathon.bebright.posts;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.hackathon.bebright.clients.users.User;
import com.hackathon.bebright.clients.users.UserClient;
import com.hackathon.bebright.posts.exceptions.InvalidRequestException;
import com.hackathon.bebright.posts.exceptions.PostNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostService {

    private final UserClient userClient;
    private final PostRepository postRepository;

    public Post addPost(String bearerToken, Post post) {
        checkPostInputProperties(post);
        Post addPost = new Post(getUsername(bearerToken), post.getDescription(), post.getImageURL());
        return postRepository.insert(addPost);
    }

    public void deletePostById(String postId) {
        Post existingPost = getPostOrThrowNull(postId); // Check post exists first
        postRepository.delete(existingPost);
    }

    public Post updatePostById(String postId, Post updatedPost) {
        Post existingPost = postRepository.findById(postId).get();
        existingPost.setDescription(updatedPost.getDescription());
        existingPost.setImageURL(updatedPost.getImageURL());
        return postRepository.save(existingPost);
    }

    public List<Post> getAllPosts() {
        List<Post> postList = postRepository.findAll();
        if (postList == null){
            throw new InvalidRequestException("There were no posts found");
        }
        return postList;
    }

    public Post getPostById(String postId) {
        return getPostOrThrowNull(postId);
    }


    public List<Post> getPostsByUser(String bearerToken) {
        String username = getUsername(bearerToken);
        return postRepository.findByUsername(username);
    }

    public List<Post> getPostsByOffice(String bearerToken, String office) {
        User user = userClient.getUserByUsername(getUsername(bearerToken));

        if (!user.getOffices().contains(office)) {
            throw new InvalidRequestException("You do not belong to this office");
        }

        List<String> usernameList = userClient.getUsernamesByOffice(office); // Filter our users by office

//        List<String> usernameList = userList.stream().map(individualUser -> individualUser.getUsername()).collect(Collectors.toList()); // From these users, we find their usernames

        return postRepository.findByUsername(usernameList);
    }

    public List<Post> getPostsByOfficeAndTeam(String office, String team) {
        List<String> usernameList = userClient.getUsernamesByOfficeAndTeam(office, team); // Filter our users by office and team

//        List<String> usernameList = userList.stream().map(individualUser -> individualUser.getUsername()).collect(Collectors.toList());
        // From these users, we find their usernames

        return postRepository.findByUsername(usernameList);
    }

    private Post getPostOrThrowNull(String postId){
        if (postId == null || postId.isEmpty()){
            throw new PostNotFoundException("Post id is invalid");
        }
        return postRepository.findById(postId).orElseThrow();
    }

    private void checkPostInputProperties(Post post) {
        if (post.getDescription() == null) {
            throw new InvalidRequestException("Description cannot be null");
        }
    }

    private String getUsername(String bearerToken) {
        String accessToken = bearerToken.split(" ")[1];
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(accessToken);
        String username = decodedJWT.getSubject();
        return username;
    }

}
