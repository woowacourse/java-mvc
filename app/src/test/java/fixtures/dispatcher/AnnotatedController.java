package fixtures.dispatcher;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class AnnotatedController {

    @RequestMapping(value = "/annotation", method = RequestMethod.GET)
    public ModelAndView show(final HttpServletRequest request, final HttpServletResponse response) {
        return new ModelAndView(new JspView("/annotation.jsp"))
                .addObject("name", request.getParameter("name"));
    }

    @RequestMapping(value = "/annotation-redirect", method = RequestMethod.POST)
    public ModelAndView redirect(final HttpServletRequest request, final HttpServletResponse response) {
        return new ModelAndView(new JspView("redirect:/annotation"));
    }

    @RequestMapping(value = "/annotation-failure", method = RequestMethod.GET)
    public ModelAndView fail(final HttpServletRequest request, final HttpServletResponse response) {
        throw new IllegalStateException("controller failed");
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView home(final HttpServletRequest request, final HttpServletResponse response) {
        return new ModelAndView(new JspView("/annotation-home.jsp"));
    }
}
