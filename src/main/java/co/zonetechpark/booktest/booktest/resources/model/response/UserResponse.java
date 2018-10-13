package co.zonetechpark.booktest.booktest.resources.model.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class UserResponse {

    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Boolean status;
    private Date dateCreated;

}
