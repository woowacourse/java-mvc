package nextstep.mvc.view;

import jakarta.servlet.http.HttpServletRequest;

public interface ViewResolver {

    View resolveViewName(String viewName, HttpServletRequest httpServletRequest) throws Exception;

}
