package webmvc.org.springframework.web.servlet.exception;

public class NoSuchHandlerFoundException extends RuntimeException {

    public NoSuchHandlerFoundException() {
        super("No Such Handler For Request");
    }
}
