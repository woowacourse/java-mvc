package webmvc.org.springframework.web.servlet.mvc.supports.resolver;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import webmvc.org.springframework.web.servlet.mvc.View;
import webmvc.org.springframework.web.servlet.mvc.supports.ViewResolver;

public class ViewResolvers {

    private final List<ViewResolver> resolvers = new ArrayList<>();

    public ViewResolvers addResolvers(final ViewResolver targetViewResolver) {
        resolvers.add(targetViewResolver);

        return this;
    }

    public boolean isEmpty() {
        return resolvers.isEmpty();
    }

    public View findView(final HttpServletRequest request, final String viewName) {
        for (final ViewResolver resolver : resolvers) {
            if (resolver.supports(request, viewName)) {
                return resolver.resolve(viewName);
            }
        }

        return null;
    }
}
