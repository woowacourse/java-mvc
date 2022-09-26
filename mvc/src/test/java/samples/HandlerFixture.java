package samples;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HandlerFixture {

    public ModelAndView modelAndViewHandler(final HttpServletRequest request, final HttpServletResponse response) {
        return new ModelAndView(new JspView("index.jsp"));
    }

    public String viewNameHandler(final HttpServletRequest request, final HttpServletResponse response) {
        return "index.jsp";
    }

    public void voidHandler(final HttpServletRequest request, final HttpServletResponse response) {
    }
}
