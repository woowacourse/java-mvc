package nextstep.mvc.view;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.Set;

public class JspViewResolver implements ViewResolver {

    @Override
    public View resolveViewName(String viewName, HttpServletRequest request) throws Exception {
        Set<String> resourcePaths = request.getSession().getServletContext().getResourcePaths("/");
        if (Objects.isNull(viewName)) {
            return null;
        }

        String pureViewName = viewName.replace(JspView.REDIRECT_PREFIX, "");

        if (resourcePaths.stream().anyMatch(it -> it.startsWith(pureViewName))) {
            return new JspView(viewName);
        }
        return null;
    }

}
