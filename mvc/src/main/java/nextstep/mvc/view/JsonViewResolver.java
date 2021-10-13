package nextstep.mvc.view;

public class JsonViewResolver implements ViewResolver {

    @Override
    public boolean supports(Class<?> returnType) {
        return !returnType.equals(String.class);
    }

    @Override
    public View resolve(Object object) {
        return new JsonView();
    }
}
