package tleaf.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//by default RuntimeException gives us response with 500 status code (Internal Server Error)
//HttpStatus.NOT_FOUND = 404
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Tweet Not Found")
public class TweetNotFoundException extends RuntimeException {
}
