package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import webmvc.org.springframework.web.servlet.ModelAndView;

public interface HandlerAdapter {

    boolean canAdapt(final Object controller);
    ModelAndView adapt(final Object controller, final HttpServletRequest req, final HttpServletResponse res)
            throws InvocationTargetException, IllegalAccessException;
}
