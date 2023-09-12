package webmvc;

public class UnsupportedHandlerException extends RuntimeException {
    public UnsupportedHandlerException() {
        super("unsupported handler");
    }
}
