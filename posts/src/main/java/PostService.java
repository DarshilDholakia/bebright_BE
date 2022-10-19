import java.util.List;

public class PostService {

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

    public List<Post> getPostsByOffice(String office) {
        return null;
    }

    public List<Post> getPostsByTeam(String office, String team) {
        return null;
    }
}
