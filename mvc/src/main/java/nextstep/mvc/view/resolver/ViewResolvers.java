package nextstep.mvc.view.resolver;

import nextstep.mvc.exception.UnHandledRequestException;
import nextstep.mvc.view.View;
import nextstep.mvc.view.ViewName;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewResolvers {

    private final List<ViewResolver> viewResolvers;
    private final Map<ViewName, View> container;

    public ViewResolvers(List<ViewResolver> viewResolvers, Map<ViewName, View> container) {
        this.viewResolvers = viewResolvers;
        this.container = container;
    }

    public ViewResolvers(List<ViewResolver> viewResolvers) {
        this(viewResolvers, new HashMap<>());
    }

    public View resolve(ViewName viewName) {
        if (container.containsKey(viewName)) {
            return container.get(viewName);
        }

        ViewResolver viewResolver = findViewResolver(viewName);
        return viewResolver.resolve(viewName);
    }

    private ViewResolver findViewResolver(ViewName viewName) {
        return viewResolvers.stream()
                .filter(resolver -> resolver.supports(viewName))
                .findAny()
                .orElseThrow(() -> new UnHandledRequestException("처리할 수 없는 뷰 형식입니다."));
    }
}
