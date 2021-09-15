package nextstep.mvc.view;

public interface ViewResolver {

    boolean supports(String viewName);

    View resolveViewName(String viewName);
}