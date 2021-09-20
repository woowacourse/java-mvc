package nextstep.mvc.servlet;

import java.util.List;
import nextstep.mvc.view.JsonView;
import nextstep.mvc.view.View;
import nextstep.mvc.view.viewresolver.ViewResolver;

public class ViewResolverRegistry {

    private final List<ViewResolver> viewResolvers;

    public ViewResolverRegistry(List<ViewResolver> viewResolvers) {
        this.viewResolvers = viewResolvers;
    }

    public View resolveView(Object handleResult) {
        return viewResolvers.stream()
            .filter(viewResolver -> viewResolver.support(handleResult.getClass()))
            .map(viewResolver -> viewResolver.resolve(handleResult))
            .findAny()
            .orElseGet(JsonView::new);
    }
}
