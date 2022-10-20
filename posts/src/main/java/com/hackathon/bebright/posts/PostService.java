package com.hackathon.bebright.posts;

import com.hackathon.bebright.clients.users.User;
import com.hackathon.bebright.clients.users.UserClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PostService {

    private final UserClient userClient;
    private final PostRepository postRepository;

    public Post addPost(Post post) {
        return post;
    }

    public void deletePostById(String postId) {
    }

    public Post updatePostById(String postId, Post updatedPost) {
        return updatedPost;
    }

    public List<Post> getAllPosts() {
        return null;
    }

    public Post getPostById(String postId) {
        return null;
    }


    public List<Post> getPostsByUser(String userId) {
        return null;
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

    public List<Post> getPostsByTeam(String office, String team) {
        return null;
    }
}
