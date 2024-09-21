package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.handler.adapter.AbstractHandlerAdapter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ManualHandlerAdapter extends AbstractHandlerAdapter<Controller> {

    public ManualHandlerAdapter() {
        super(Controller.class);
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Controller handler)
            throws Exception {
        String viewName = handler.execute(request, response);

        return ModelAndView.createWithJspView(viewName);
    }
}
