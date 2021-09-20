package nextstep.mvc.exception;

public class AdapterNotFoundException extends RuntimeException {

    public AdapterNotFoundException(Object handler) {
        super(String.format("Unable to find handler adapter. [Handler]: %s", handler.toString()));
    }
}
