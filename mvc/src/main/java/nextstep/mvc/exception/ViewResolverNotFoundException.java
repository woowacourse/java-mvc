package nextstep.mvc.exception;

public class ViewResolverNotFoundException extends MvcException {

    public ViewResolverNotFoundException(String viewName) {
        super(String.format("No view resolver found for view %s", viewName));
    }
}
