package webmvc.org.springframework.web.servlet.mvc.tobe;

import webmvc.org.springframework.web.servlet.View;
import webmvc.org.springframework.web.servlet.view.JsonView;

public class JsonViewResolver implements ViewResolver {

    @Override
    public boolean supports(final Object view) {
        return view == null || view instanceof JsonView;
    }

    @Override
    public View resolve(final Object view) {
        return new JsonView();
    }
}
