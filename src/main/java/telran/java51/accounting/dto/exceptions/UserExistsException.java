package telran.java51.accounting.dto.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserExistsException extends RuntimeException {
    private static final long serialVersionUID = 1;
    public UserExistsException() {
        super("User with such login already exists");
    }
}