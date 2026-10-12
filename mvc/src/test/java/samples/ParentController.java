package samples;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ParentController {

    private static final Logger log = LoggerFactory.getLogger(ParentController.class);

    @RequestMapping(value = "/parent", method = RequestMethod.GET)
    public ModelAndView parent(final HttpServletRequest request, final HttpServletResponse response) {
        log.info("parent controller parent method");
        final ModelAndView modelAndView = new ModelAndView(new JspView(""));
        modelAndView.addObject("handler", "parent");
        return modelAndView;
    }

    @RequestMapping(value = "/parent-pacakge-private", method = RequestMethod.GET)
    private ModelAndView parentPackagePrivate(final HttpServletRequest request, final HttpServletResponse response) {
        log.info("parent controller parent-package-private method");
        final ModelAndView modelAndView = new ModelAndView(new JspView(""));
        modelAndView.addObject("handler", "parent-package-private");
        return modelAndView;
    }

    @RequestMapping(value = "/parent-private", method = RequestMethod.GET)
    private ModelAndView parentPrivate(final HttpServletRequest request, final HttpServletResponse response) {
        log.info("parent controller parent-private method");
        final ModelAndView modelAndView = new ModelAndView(new JspView(""));
        modelAndView.addObject("handler", "parent-private");
        return modelAndView;
    }

    @RequestMapping(value = "/overridden", method = RequestMethod.GET)
    public ModelAndView overridden(final HttpServletRequest request, final HttpServletResponse response) {
        log.info("parent controller overridden method");
        final ModelAndView modelAndView = new ModelAndView(new JspView(""));
        modelAndView.addObject("handler", "overridden");
        return modelAndView;
    }
}

