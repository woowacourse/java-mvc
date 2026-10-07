package nonpublicmethods;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class NonPublicMethodController {

    @RequestMapping("/private-test")
    private ModelAndView privateHandler(HttpServletRequest request, HttpServletResponse response) {
        return new ModelAndView(new JspView(""))
                .addObject("id", request.getAttribute("id"))
                .addObject("visibility", "private");
    }

    @RequestMapping("/protected-test")
    protected ModelAndView protectedHandler(HttpServletRequest request, HttpServletResponse response) {
        return new ModelAndView(new JspView(""))
                .addObject("id", request.getAttribute("id"))
                .addObject("visibility", "protected");
    }

    @RequestMapping("/package-private-test")
    ModelAndView packagePrivateHandler(HttpServletRequest request, HttpServletResponse response) {
        return new ModelAndView(new JspView(""))
                .addObject("id", request.getAttribute("id"))
                .addObject("visibility", "package-private");
    }
}
