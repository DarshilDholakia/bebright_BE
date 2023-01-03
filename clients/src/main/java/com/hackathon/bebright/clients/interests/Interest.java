package com.hackathon.bebright.clients.interests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "interest")
public class Interest implements Serializable {

    private static final long serialVersionUID = 7156526077883281623L;

    @Id
    private String interestId;
    private String username;
    private String interestType;

    public Interest(String username, String interestType) {
        this.username = username;
        this.interestType = interestType;
    }


}
