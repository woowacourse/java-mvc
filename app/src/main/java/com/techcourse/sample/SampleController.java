package com.techcourse.sample;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JsonView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class SampleController {

    @RequestMapping(value = "/sample", method = RequestMethod.GET)
    public ModelAndView forward(final HttpServletRequest request, final HttpServletResponse response) {

        final ModelAndView modelAndView = new ModelAndView(new JsonView());
        modelAndView.addObject("greeting", "hi potato~");
        return modelAndView;
    }
}
