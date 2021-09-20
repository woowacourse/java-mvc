package nextstep.mvc.view.viewresolver;

import nextstep.mvc.view.JspView;
import nextstep.mvc.view.View;

public class JspViewResolver implements ViewResolver {

    @Override
    public boolean support(Class<?> returnType) {
        return returnType.equals(String.class);
    }

    @Override
    public View resolve(Object object) {
        String url = (String) object;
        return new JspView(url);
    }
}
