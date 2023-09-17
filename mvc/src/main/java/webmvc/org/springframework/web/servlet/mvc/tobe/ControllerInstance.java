package webmvc.org.springframework.web.servlet.mvc.tobe;

public class ControllerInstance {

    private final Object instance;
    private final String uriPrefix;

    public ControllerInstance(Object instance, String uriPrefix) {
        this.instance = instance;
        this.uriPrefix = uriPrefix;
    }

    public Object getInstance() {
        return instance;
    }

    public String getUriPrefix() {
        return uriPrefix;
    }
}
