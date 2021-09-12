package nextstep.mvc.exception;

public class NotFoundException extends RuntimeException {

    private static final String MSG = "404";

    public NotFoundException() {
        super(MSG);
    }
}
