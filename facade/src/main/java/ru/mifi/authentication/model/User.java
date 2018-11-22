package ru.mifi.authentication.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.SafeHtml;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.Set;


@Data
@RequiredArgsConstructor
public class User {

    private String id;

    protected String userName;

    protected String firstName;

    protected String lastName;

    @NotBlank
    @Email
    @SafeHtml
    private String email;

    @NotBlank
    @Length(min = 5)
    private String password;

    private boolean enabled = true;

    private Date registered = new Date();

//    @Enumerated(EnumType.STRING)
//    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Role> roles;

}
