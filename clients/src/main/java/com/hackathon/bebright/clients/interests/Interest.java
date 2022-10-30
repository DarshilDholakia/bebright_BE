package com.hackathon.bebright.clients.interests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "interest")
public class Interest {

    @Id
    private String interestId;
    private String username;
    private String interestType;

    public Interest(String username, String interestType) {
        this.username = username;
        this.interestType = interestType;
    }
}
