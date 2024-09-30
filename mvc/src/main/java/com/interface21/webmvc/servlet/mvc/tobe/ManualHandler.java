package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;

public class ManualHandler implements Handler{

    private Controller controller;

    public ManualHandler(Object object) {
        this.controller = (Controller) object;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String viewName = controller.execute(request, response);
        View view = new JspView(viewName);
        view.render(new HashMap<>(), request, response);
    }
}
