package nextstep.mvc.view;

public interface ViewResolver {

    boolean supports(Class<?> returnType);

    View resolve(Object object);
}
