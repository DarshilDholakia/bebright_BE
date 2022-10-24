package com.hackathon.bebright.posts;

import com.hackathon.bebright.clients.users.User;
import com.hackathon.bebright.clients.users.UserClient;
import com.hackathon.bebright.posts.Exceptions.InvalidRequestException;
import com.hackathon.bebright.posts.Exceptions.PostNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.auditing.CurrentDateTimeProvider;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PostService {

    private final UserClient userClient;
    private final PostRepository postRepository;

    public Post addPost(Post post) {
        checkPostInputProperties(post);
        Post addPost = new Post(post.getUserId(), post.getDescription(), post.getImageURL());
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


    public List<Post> getPostsByUser(String userId) {
        return postRepository.findByUserId(userId);
    }

    public List<List<Post>> getPostsByOffice(String office) {
        List<User> userList = userClient.getUsersByOffice(office).getBody(); // Filter our users by office

        List<String> userIdList = new ArrayList<>();
        userList.forEach(user -> userIdList.add(user.getUserId())); // From these users, we find their ids

        List<List<Post>> postList = new ArrayList<>();
        userIdList.forEach(userId -> postList.add(postRepository.findByUserId(userId))); // Collate the posts of the relevant
        // users using their ids. List of lists used because we gather a list of posts for one user,
        // then a list of these for each user
        return postList;
    }

    public List<List<Post>> getPostsByTeam(String office, String team) {
        List<User> userList = userClient.getUsersByOfficeAndTeam(office, team).getBody(); // Filter our users by office and team

        List<String> userIdList = new ArrayList<>();
        userList.forEach(user -> userIdList.add(user.getUserId())); // From these users, we find their ids

        List<List<Post>> postList = new ArrayList<>();
        userIdList.forEach(userId -> postList.add(postRepository.findByUserId(userId))); // Collate the posts of the relevant
        // users using their ids. List of lists used because we gather a list of posts for one user,
        // then a list of these for each user
        return postList;
    }

    private Post getPostOrThrowNull(String postId){
        if (postId == null || postId.isEmpty()){
            throw new PostNotFoundException("Post id is invalid");
        }
        return postRepository.findById(postId).orElseThrow();
    }

    private void checkPostInputProperties(Post post) {
        if (post.getUserId() == null) {
            throw new InvalidRequestException("User id cannot be null");
        }
        if (post.getDescription() == null) {
            throw new InvalidRequestException("Description cannot be null");
        }
    }

}
