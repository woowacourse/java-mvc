package samples;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.DeleteMapping;
import web.org.springframework.web.bind.annotation.GetMapping;
import web.org.springframework.web.bind.annotation.HeadMapping;
import web.org.springframework.web.bind.annotation.PatchMapping;
import web.org.springframework.web.bind.annotation.PostMapping;
import web.org.springframework.web.bind.annotation.PutMapping;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.view.JspView;

@Controller
public class TestAnnotationController {

    private static final Logger log = LoggerFactory.getLogger(TestAnnotationController.class);

    @GetMapping("/get-test")
    public ModelAndView findUserId(final HttpServletRequest request, final HttpServletResponse response) {
        log.info("test controller get method");
        final var modelAndView = new ModelAndView(new JspView(""));
        modelAndView.addObject("id", request.getAttribute("id"));
        return modelAndView;
    }

    @PostMapping(value = "/post-test")
    public ModelAndView save(final HttpServletRequest request, final HttpServletResponse response) {
        log.info("test controller post method");
        final var modelAndView = new ModelAndView(new JspView(""));
        modelAndView.addObject("id", request.getAttribute("id"));
        return modelAndView;
    }

    @DeleteMapping(value = "/delete-test")
    public ModelAndView delete(final HttpServletRequest request, final HttpServletResponse response) {
        log.info("test controller delete method");
        final var modelAndView = new ModelAndView(new JspView(""));
        modelAndView.addObject("id", request.getAttribute("id"));
        return modelAndView;
    }

    @PatchMapping(value = "/patch-test")
    public ModelAndView patch(final HttpServletRequest request, final HttpServletResponse response) {
        log.info("test controller patch method");
        final var modelAndView = new ModelAndView(new JspView(""));
        modelAndView.addObject("id", request.getAttribute("id"));
        return modelAndView;
    }

    @PutMapping(value = "/put-test")
    @PatchMapping(value = "/patch-second-test")
    public ModelAndView put(final HttpServletRequest request, final HttpServletResponse response) {
        log.info("test controller put method");
        log.info("test controller patch-second method");
        final var modelAndView = new ModelAndView(new JspView(""));
        modelAndView.addObject("id", request.getAttribute("id"));
        return modelAndView;
    }

    @HeadMapping(value = "/head-test")
    public ModelAndView head(final HttpServletRequest request, final HttpServletResponse response) {
        log.info("test controller head method");
        final var modelAndView = new ModelAndView(new JspView(""));
        modelAndView.addObject("id", request.getAttribute("id"));
        return modelAndView;
    }
}
