package samples;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class TestAnnotationMvcController {

    @RequestMapping(value = "/test-annotation-mvc", method = RequestMethod.GET)
    public ModelAndView show(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView =  new ModelAndView(new JspView(""));
        modelAndView.addObject("id", request.getAttribute("id"));
        return modelAndView;
    }
}
