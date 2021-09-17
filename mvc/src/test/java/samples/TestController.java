package samples;

import com.techcourse.air.core.annotation.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.techcourse.air.mvc.core.view.JsonView;
import com.techcourse.air.mvc.core.view.JspView;
import com.techcourse.air.mvc.core.view.ModelAndView;
import com.techcourse.air.mvc.web.annotation.RequestMapping;
import com.techcourse.air.mvc.web.annotation.ResponseBody;
import com.techcourse.air.mvc.web.support.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class TestController {

    private static final Logger log = LoggerFactory.getLogger(TestController.class);

    @RequestMapping(value = "/get-test", method = RequestMethod.GET)
    public ModelAndView findUserId(HttpServletRequest request, HttpServletResponse response) {
        log.info("test controller get method");
        final ModelAndView modelAndView = new ModelAndView(new JspView(""));
        modelAndView.addObject("id", request.getAttribute("id"));
        return modelAndView;
    }

    @RequestMapping(value = "/post-test", method = RequestMethod.POST)
    public ModelAndView save(HttpServletRequest request, HttpServletResponse response) {
        log.info("test controller post method");
        final ModelAndView modelAndView = new ModelAndView(new JspView(""));
        modelAndView.addObject("id", request.getAttribute("id"));
        return modelAndView;
    }

    // @ResponseBody & String을 리턴하는 컨트롤러
    @RequestMapping(value = "/json/string", method = RequestMethod.GET)
    @ResponseBody
    public String jsonString(HttpServletRequest request, HttpServletResponse response) {
        return "ok";
    }

    // @ResponseBody & Object를 리턴하는 컨트롤러
    @RequestMapping(value = "/json/object", method = RequestMethod.GET)
    @ResponseBody
    public User jsonObject(HttpServletRequest request, HttpServletResponse response) {
        return new User(1L, "air", "1234", "air.junseo@gmail.com");
    }

    // ModelAndView를 통해 JsonView를 렌더링하는 컨트롤러(모델 1개)
    @RequestMapping(value = "/json/view", method = RequestMethod.GET)
    public ModelAndView jsonView(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView(new JsonView());
        User user = new User(1L, "air", "1234", "air.junseo@gmail.com");
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    // ModelAndView를 통해 JsonView를 렌더링하는 컨트롤러(모델 2개)
    @RequestMapping(value = "/json/view2", method = RequestMethod.GET)
    public ModelAndView jsonView2(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView(new JsonView());
        User user1 = new User(1L, "air", "1234", "air.junseo@gmail.com");
        User user2 = new User(1L, "air", "1234", "air.junseo@gmail.com");
        modelAndView.addObject("user1", user1);
        modelAndView.addObject("user2", user2);
        return modelAndView;
    }
}
