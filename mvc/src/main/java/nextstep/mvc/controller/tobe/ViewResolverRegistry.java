package nextstep.mvc.controller.tobe;

import java.util.ArrayList;
import java.util.List;
import nextstep.mvc.ViewResolver;

public class ViewResolverRegistry {

    private final List<ViewResolver> viewResolvers;

    public ViewResolverRegistry() {
        this.viewResolvers = new ArrayList<>();
    }

    public void addViewResolver(final ViewResolver viewResolver) {
        viewResolvers.add(viewResolver);
    }

    public ViewResolver getViewResolver() {
        return viewResolvers.get(0);
    }
}
