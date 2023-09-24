package webmvc.org.springframework.web.servlet.mvc.exception;

public class NotFoundHandlerAdapter extends IllegalArgumentException {

    public NotFoundHandlerAdapter(final String message) {
        super(message);
    }
}
