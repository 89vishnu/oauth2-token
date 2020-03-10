package com.miworld.mi.auth.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@Document(collection= "users")
public class User {

    @Id
    private Long id;

    private String firstName;
    private String lastName;

    @Indexed(unique = true)
    private String username;

    private String password;

//    @DBRef
//    private School school;

    private String title;
    private boolean mi;
    private List<Role> roles;
    private Date createdOn;

//    private ContactDetails contactDetails;

}
