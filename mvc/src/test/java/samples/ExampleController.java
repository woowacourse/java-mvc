package samples;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Objects;

public class ExampleController {

    @RequestMapping(value = "/get-test", method = {RequestMethod.DELETE, RequestMethod.GET})
    public ModelAndView method1(HttpServletRequest request, HttpServletResponse response) {
        return new ModelAndView(new JspView(""));
    }

    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public ModelAndView method2(HttpServletRequest request, HttpServletResponse response) {
        return new ModelAndView(new JspView(""));
    }

    public ModelAndView method3(HttpServletRequest request, HttpServletResponse response) {
        return new ModelAndView(new JspView(""));
    }

    @RequestMapping(value = "/all")
    public ModelAndView method4(HttpServletRequest request, HttpServletResponse response) {
        return new ModelAndView(new JspView(""));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        return o != null && getClass() == o.getClass();
    }

    @Override
    public int hashCode() {
        return Objects.hash();
    }
}
