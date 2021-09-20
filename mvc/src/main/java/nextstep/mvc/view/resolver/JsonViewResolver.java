package nextstep.mvc.view.resolver;

import nextstep.mvc.view.JsonView;
import nextstep.mvc.view.View;
import nextstep.mvc.view.ViewName;

public class JsonViewResolver implements ViewResolver {

    private final View JSON_VIEW = new JsonView();

    @Override
    public boolean supports(ViewName viewName){
        return viewName.isEmpty();
    }

    @Override
    public View resolve(ViewName fileName) {
        return JSON_VIEW;
    }
}
