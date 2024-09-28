package samples;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class TestMethod {

    public ModelAndView test(final HttpServletRequest request, final HttpServletResponse response) {
        return new ModelAndView(new JspView(""));
    }

    public ModelAndView wrongParameterSize(final HttpServletRequest request, final HttpServletResponse response, final String name) {
        return new ModelAndView(new JspView(""));
    }

    public ModelAndView wrongRequest(final String request, final HttpServletResponse response) {
        return new ModelAndView(new JspView(""));
    }

    public ModelAndView wrongResponse(final HttpServletRequest request, final String response) {
        return new ModelAndView(new JspView(""));
    }

    public String wrongReturnType(final HttpServletRequest request, final HttpServletResponse response) {
        return "";
    }
}
