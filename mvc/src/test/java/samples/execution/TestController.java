package samples.execution;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class TestController {

    public ModelAndView findUserId(
            final HttpServletRequest request,
            final HttpServletResponse response
    ) {
        final ModelAndView modelAndView = new ModelAndView(new JspView(""));
        modelAndView.addObject("id", request.getAttribute("id"));
        return modelAndView;
    }

    public void throwException(
            final HttpServletRequest request,
            final HttpServletResponse response
    ) {
        throw new IllegalStateException();
    }
}
