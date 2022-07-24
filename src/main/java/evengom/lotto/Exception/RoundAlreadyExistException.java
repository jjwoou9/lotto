package evengom.lotto.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.IM_USED)
public class RoundAlreadyExistException extends RuntimeException {
    public RoundAlreadyExistException(String message) {
        super(message);
    }
}
