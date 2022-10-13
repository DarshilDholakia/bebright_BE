import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data // does getters and setters, toString etc for you
@AllArgsConstructor // creates a constructor with all arguments
@NoArgsConstructor
@Document(collection = "user") // enables us to specify that this class will be a collection (tables equivalent in mongoDb)
public class Post {

    @Id
    private String postId;
    private String userId;
    private String description;
    private String imageURL;
    private Integer likes;
    private String[] comments;

}
