package samples.handlerExecution;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.tobe.HandlerExecution;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;

@RequestMapping(value = "/post-test", method = RequestMethod.POST)
public class PostTestHandlerExecution extends HandlerExecution {
    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String attributeName = "id";
        Object id = request.getAttribute(attributeName);

        ModelAndView modelAndView = new ModelAndView(null);

        modelAndView.addObject(attributeName, id);

        return modelAndView;
    }
}
