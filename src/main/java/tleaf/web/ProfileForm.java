package tleaf.web;

import org.hibernate.validator.constraints.Email;
import org.springframework.web.multipart.MultipartFile;
import tleaf.Profile;
import tleaf.validate.EmailValidator;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ProfileForm {

    @NotNull
    @Size(min=5, max=16, message="{username.size}")
    private String username;

    @NotNull
    @Size(min=5, max=25, message="{password.size}")
    private String password;

    @NotNull
    @EmailValidator
    private String email;

    private MultipartFile profilePicture;

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

    public MultipartFile getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(MultipartFile profilePicture) {
        this.profilePicture = profilePicture;
    }

    public Profile toUser() {
        return new Profile(username, password, email);
    }
}
