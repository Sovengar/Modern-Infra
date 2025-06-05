package jon.modern_infra.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

@Getter
public class RootException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 6378336966214073013L;

    private final HttpStatus httpStatus;
    private final String messageToBeDisplayed;
    private final List<ErrorDetails> errors = new ArrayList<>();

    public RootException(final String message) {
        super(message);
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        this.messageToBeDisplayed = null;
    }

    public RootException(final HttpStatus httpStatus, final String messageTokenForI18n) {
        super();
        this.httpStatus = httpStatus;
        this.messageToBeDisplayed = messageTokenForI18n;
    }

    public RootException(final HttpStatus httpStatus, final String message, final String messageTokenForI18n) {
        super(message);
        this.httpStatus = httpStatus;
        this.messageToBeDisplayed = messageTokenForI18n;
    }

    public RootException(final HttpStatus httpStatus, final String message, String messageTokenForI18n, Throwable cause) {
        super(message, cause);
        this.httpStatus = httpStatus;
        this.messageToBeDisplayed = messageTokenForI18n;
    }
}
