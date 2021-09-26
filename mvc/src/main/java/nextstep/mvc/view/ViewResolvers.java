package nextstep.mvc.view;

import nextstep.mvc.exception.ViewResolverNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class ViewResolvers {

    private final List<ViewResolver> resolvers;

    public ViewResolvers() {
        this(new ArrayList<>());
    }

    public ViewResolvers(List<ViewResolver> resolvers) {
        this.resolvers = resolvers;
    }

    public View resolve(String viewName) {
        String viewFileName = ViewUtils.removeRedirect(viewName);
        return resolvers.stream()
                .filter(resolver -> resolver.supports(viewFileName))
                .findFirst()
                .orElseThrow(() -> new ViewResolverNotFoundException(viewName))
                .resolve(viewName);
    }

    public void addViewResolver(ViewResolver viewResolver) {
        resolvers.add(viewResolver);
    }
}
