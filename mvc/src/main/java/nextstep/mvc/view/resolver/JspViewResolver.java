package nextstep.mvc.view.resolver;

import nextstep.mvc.view.JspView;
import nextstep.mvc.view.View;

public class JspViewResolver implements ViewResolver {

    private static final String REDIRECT_PREFIX = "redirect:";
    private static final String JSP_SUFFIX = ".jsp";

    @Override
    public View resolve(final Object view) {
        if (view instanceof String && (startsWithRedirect(view) || endsWithJsp(view))) {
            return new JspView((String) view);
        }

        return null;
    }

    private static boolean startsWithRedirect(final Object view) {
        return ((String) view).startsWith(REDIRECT_PREFIX);
    }

    private boolean endsWithJsp(final Object view) {
        return ((String) view).endsWith(JSP_SUFFIX);
    }
}
