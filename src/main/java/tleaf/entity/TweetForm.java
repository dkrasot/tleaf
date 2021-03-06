package tleaf.entity;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class TweetForm {

    @NotNull
    @Size(min = 1, max = 140)
    private String message;

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

}
