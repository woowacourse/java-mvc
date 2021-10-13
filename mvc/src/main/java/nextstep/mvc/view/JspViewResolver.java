package nextstep.mvc.view;

import java.util.Locale;

public class JspViewResolver implements ViewResolver {

    @Override
    public boolean supports(Class<?> returnType) {
        return returnType.equals(String.class);
    }

    @Override
    public View resolve(Object object) {
        return new JspView((String) object);
    }
}
