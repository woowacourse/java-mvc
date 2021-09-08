package nextstep.mvc.controller.tobe;

public class ResourceHandler {

    private final String resourcePath;

    public ResourceHandler(final String resourcePath) {
        this.resourcePath = resourcePath;
    }

    public String handle() {
        return resourcePath;
    }
}
