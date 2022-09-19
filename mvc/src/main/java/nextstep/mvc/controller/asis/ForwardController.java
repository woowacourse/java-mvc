package nextstep.mvc.controller.asis;

import java.util.Objects;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;

@nextstep.web.annotation.Controller
public class ForwardController implements Controller {

	private final String path;

	public ForwardController(final String path) {
		this.path = Objects.requireNonNull(path);
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String execute(final HttpServletRequest request, final HttpServletResponse response) {
		return path;
	}
}
