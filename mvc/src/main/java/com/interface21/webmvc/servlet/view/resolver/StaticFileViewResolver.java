package com.interface21.webmvc.servlet.view.resolver;

import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.view.StaticFileView;
import java.util.Set;

public class StaticFileViewResolver implements ViewResolver {

    private static final Set<String> STATIC_EXTENSIONS =
            Set.of(".html", ".css", ".js", ".png", ".jpg", ".jpeg", ".gif", ".svg", ".ico");

    @Override
    public View resolveViewName(final String viewName) {
        if (!isStaticFile(viewName)) {
            return null;
        }
        return new StaticFileView(normalize(viewName));
    }

    private boolean isStaticFile(final String viewName) {
        final String lower = viewName.toLowerCase();
        return STATIC_EXTENSIONS.stream().anyMatch(lower::endsWith);
    }

    private String normalize(final String viewName) {
        return viewName.startsWith("/") ? viewName : "/" + viewName;
    }
}
