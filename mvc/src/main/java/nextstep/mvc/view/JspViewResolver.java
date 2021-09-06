package nextstep.mvc.view;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Set;

public class JspViewResolver implements ViewResolver {

    @Override
    public View resolveViewName(String viewName, HttpServletRequest request) throws Exception {
        Set<String> resourcePaths = request.getSession().getServletContext().getResourcePaths("/");

        if (resourcePaths.contains(viewName.replace(JspView.REDIRECT_PREFIX, ""))) {
            return new JspView(viewName);
        }
        return null;
    }

}
