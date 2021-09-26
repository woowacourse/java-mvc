package nextstep.mvc.view;

import nextstep.mvc.exception.ViewResolverNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ViewResolvers {

    private static final Logger log = LoggerFactory.getLogger(ViewResolvers.class);

    private final List<ViewResolver> resolvers;

    public ViewResolvers() {
        this(new ArrayList<>());
    }

    public ViewResolvers(List<ViewResolver> resolvers) {
        this.resolvers = resolvers;
    }

    public void init() {
        log.info("Adding default view resolvers");
        addDefaultViewResolvers();
    }

    private void addDefaultViewResolvers() {
        resolvers.add(new JspFileViewResolver("app/webapp"));
        log.info("added JspFileViewAdapter as default");
    }

    public View resolve(String viewName) {
        String viewFileName = ViewUtils.removeRedirect(viewName);
        return resolvers.stream()
                .filter(resolver -> resolver.supports(viewFileName))
                .findFirst()
                .orElseThrow(() -> new ViewResolverNotFoundException(viewName))
                .resolve(viewName);
    }
}
