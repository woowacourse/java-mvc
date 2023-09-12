package webmvc.org.springframework.web.servlet.mvc;

public interface HandlerMapping<T> {

    void initialize();

    Object getHandler(final T target);
}
