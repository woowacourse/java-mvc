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
public class TestController {

	private static final Logger log = LoggerFactory.getLogger(TestController.class);

	@RequestMapping(value = "/get-test", method = RequestMethod.GET)
	public ModelAndView findUserId(final HttpServletRequest request, final HttpServletResponse response) {
		log.info("test controller get method");
		final var modelAndView = new ModelAndView(new JspView(""));
		modelAndView.addObject("id", request.getAttribute("id"));
		return modelAndView;
	}

	@RequestMapping(value = "/post-test", method = RequestMethod.POST)
	public ModelAndView save(final HttpServletRequest request, final HttpServletResponse response) {
		log.info("test controller post method");
		final var modelAndView = new ModelAndView(new JspView(""));
		modelAndView.addObject("id", request.getAttribute("id"));
		return modelAndView;
	}

	@RequestMapping(value = "/update-test", method = {RequestMethod.PUT, RequestMethod.PATCH})
	public ModelAndView update(final HttpServletRequest request, final HttpServletResponse response) {
		log.info("test controller update method");
		final var modelAndView = new ModelAndView(new JspView(""));
		modelAndView.addObject("id", request.getAttribute("id"));
		return modelAndView;
	}
}
