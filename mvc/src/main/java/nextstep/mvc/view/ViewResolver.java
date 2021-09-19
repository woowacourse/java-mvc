package nextstep.mvc.view;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ViewResolver {

    private static final String REDIRECT_PREFIX = "redirect:";

    private final Map<String, View> views = new ConcurrentHashMap<>();

    public View resolverViewName(String viewName) {
        return views.computeIfAbsent(viewName, key -> {
            if (viewName.startsWith(REDIRECT_PREFIX)) {
                return new RedirectView(viewName.substring(REDIRECT_PREFIX.length()));
            }
            if (viewName.endsWith(".jsp")) {
                return new JspView(viewName);
            }
            return new JsonView();
        });
    }
}
