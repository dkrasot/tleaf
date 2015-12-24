package tleaf.entity;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import tleaf.validate.EmailValidator;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Profile {
    //    id IDENTITY,
//    username VARCHAR(20) UNIQUE not null,
//    password VARCHAR(20) not null,
//    email VARCHAR(30) not null
    private Long id;

    @NotNull
    @Size(min = 5, max = 16, message = "{username.size}")
    private String username;

    @NotNull
    @Size(min = 5, max = 25, message = "{password.size}")
    private String password;

    @NotNull
    @EmailValidator
    private String email;


    public Profile() {
    }

    public Profile(String username, String password, String email) {
        this(null, username, password, email);
    }

    public Profile(Long id, String username, String password, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object that) {
        return EqualsBuilder.reflectionEquals(this, that, new String[]{"username", "password", "email"});
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, new String[]{"username", "password", "email"});
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    //    private MultipartFile profilePicture;
//
//    public MultipartFile getProfilePicture() {
//        return profilePicture;
//    }
//
//    public void setProfilePicture(MultipartFile profilePicture) {
//        this.profilePicture = profilePicture;
//    }
}
