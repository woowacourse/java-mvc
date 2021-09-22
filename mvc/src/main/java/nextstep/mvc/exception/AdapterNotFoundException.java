package nextstep.mvc.exception;

public class AdapterNotFoundException extends Exception {

    private static final String MESSAGE = "Adapter Not Found";

    public AdapterNotFoundException() {
        super(MESSAGE);
    }
}
