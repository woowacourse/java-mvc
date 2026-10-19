package samples;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class MappingTestController {

    @RequestMapping("/all-methods")
    public ModelAndView allMethods(final HttpServletRequest request, final HttpServletResponse response) {
        return new ModelAndView(new JspView("/test.jsp"))
                .addObject("controller", this)
                .addObject("request", request)
                .addObject("response", response);
    }

    @RequestMapping(value = "/multiple-methods", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView multipleMethods(final HttpServletRequest request, final HttpServletResponse response) {
        return new ModelAndView(new JspView("/test.jsp")).addObject("controller", this);
    }

    @RequestMapping(value = "/shared-url", method = RequestMethod.GET)
    public ModelAndView get(final HttpServletRequest request, final HttpServletResponse response) {
        return new ModelAndView(new JspView("/test.jsp")).addObject("method", "GET");
    }

    @RequestMapping(value = "/shared-url", method = RequestMethod.POST)
    public ModelAndView post(final HttpServletRequest request, final HttpServletResponse response) {
        return new ModelAndView(new JspView("/test.jsp")).addObject("method", "POST");
    }

    public void withoutRequestMapping() {
        throw new AssertionError("@RequestMapping이 없는 메서드는 등록하거나 실행하지 않는다");
    }
}
