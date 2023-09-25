package webmvc.org.springframework.web.servlet.resolver;

import java.util.List;
import java.util.Optional;

public class ViewResolvers {

    private final List<ViewResolver> values;

    public ViewResolvers(final List<ViewResolver> values) {
        this.values = values;
    }

    public Optional<ViewResolver> findSupportedViewResolver(final String viewName) {
        return values.stream()
                .filter(it -> it.supports(viewName))
                .findAny();
    }
}
