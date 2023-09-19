package webmvc.org.springframework.web.servlet.mvc.view.resolver;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import webmvc.org.springframework.web.servlet.View;

public class ViewResolvers {

    private final List<ViewResolver> resolvers = new ArrayList<>();

    public void initialize() {
        final JspViewResolver jspViewResolver = new JspViewResolver();
        final JsonViewResolver jsonViewResolver = new JsonViewResolver();

        resolvers.add(jspViewResolver);
        resolvers.add(jsonViewResolver);
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
