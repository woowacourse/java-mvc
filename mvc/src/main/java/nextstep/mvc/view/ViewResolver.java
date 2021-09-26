package nextstep.mvc.view;

public interface ViewResolver {

    boolean supports(String viewName);

    View resolve(String name);
}
