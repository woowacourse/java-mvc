package samples;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.view.JspView;

@Controller
@RequestMapping("/test")
public class ClassRequestMappingTestController {

	private static final Logger log = LoggerFactory.getLogger(ClassRequestMappingTestController.class);

	@RequestMapping(value = "/get-test", method = RequestMethod.GET)
	public ModelAndView findUserId(final HttpServletRequest request, final HttpServletResponse response) {
		log.info("test controller with RequestMapping get method");
		final var modelAndView = new ModelAndView(new JspView(""));
		modelAndView.addObject("id", request.getAttribute("id"));
		return modelAndView;
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView save(final HttpServletRequest request, final HttpServletResponse response) {
		log.info("test controller with RequestMapping post method");
		final var modelAndView = new ModelAndView(new JspView(""));
		modelAndView.addObject("id", request.getAttribute("id"));
		return modelAndView;
	}
}
