package dispatcherfixtures;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class RegistryTestController {

    @RequestMapping(value = "/registry-test", method = RequestMethod.GET)
    public ModelAndView registryTest(
            final HttpServletRequest request,
            final HttpServletResponse response
    ) {
        return new ModelAndView(new JspView("/registry-test.jsp"))
                .addObject("name", "gugu");
    }

    @RequestMapping(value = "/registry-test", method = RequestMethod.POST)
    public ModelAndView registryPostTest(
            final HttpServletRequest request,
            final HttpServletResponse response
    ) {
        return new ModelAndView(new JspView("/registry-post-test.jsp"))
                .addObject("name", "post-gugu");
    }
}
