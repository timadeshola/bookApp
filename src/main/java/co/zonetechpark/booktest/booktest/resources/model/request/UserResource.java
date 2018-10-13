package co.zonetechpark.booktest.booktest.resources.model.request;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Setter
@Getter
public class UserResource {

    private Long id;

    @NotNull(message = "username cannot be empty")
    @Size(min = 3, message = "minimum length is 3")
    private String username;

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
            message = "Password must have a least a number, uppercase and lower case, special characters")
    @Size(min = 8, message = "minimum length is 8")
    private String password;

    @NotNull(message = "first name cannot be empty")
    @Size(min = 3, message = "minimum length is 3")
    private String firstName;

    @NotNull(message = "last name cannot be empty")
    @Size(min = 3, message = "minimum length is 3")
    private String lastName;

    @Email
    @NotNull
    private String email;

    @Pattern(regexp = "(^$|[0-9]{14})", message = "format template is 2348012345678 or 08012345678 or 70231123")
    @Size(min = 7, max = 14, message = "minimum length is 7 and maximum length is 14")
    private String phoneNumber;

    private List<Long> roleIds;

}
