package mappingfixtures.differentcontrollers;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class DuplicateControllers {

    @Controller
    public static class FirstController {

        @RequestMapping(value = "/users", method = RequestMethod.GET)
        public ModelAndView get(final HttpServletRequest request, final HttpServletResponse response) {
            return new ModelAndView(new JspView("/users.jsp"));
        }
    }

    @Controller
    public static class SecondController {

        @RequestMapping(value = "/users", method = RequestMethod.GET)
        public ModelAndView get(final HttpServletRequest request, final HttpServletResponse response) {
            return new ModelAndView(new JspView("/users.jsp"));
        }
    }
}
