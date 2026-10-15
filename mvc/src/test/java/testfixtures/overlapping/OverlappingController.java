package testfixtures.overlapping;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class OverlappingController {

    @RequestMapping("/overlapping")
    public ModelAndView allMethods(final HttpServletRequest request, final HttpServletResponse response) {
        return new ModelAndView(new JspView("/all.jsp"));
    }

    @RequestMapping(value = "/overlapping", method = RequestMethod.GET)
    public ModelAndView get(final HttpServletRequest request, final HttpServletResponse response) {
        return new ModelAndView(new JspView("/get.jsp"));
    }
}
