package webmvc.org.springframework.web.servlet.mvc.tobe;

public interface HandlerMapping {

    void initialize();

    boolean support(final Request request);

    Object getHandler(final Request request);
}
