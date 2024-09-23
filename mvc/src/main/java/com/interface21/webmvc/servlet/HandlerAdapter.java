package com.interface21.webmvc.servlet;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Enumeration;

/*
컨트롤러에서 반환 값을 받아서, ModelAndView로 변환한다.
 */
public class HandlerAdapter {

    public ModelAndView handle(Controller controller, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        final String result = controller.execute(request, response);

        ModelAndView modelAndView = new ModelAndView(result);
        addAttributes(request, modelAndView);
        return modelAndView;
    }

    private void addAttributes(HttpServletRequest request, ModelAndView modelAndView) {
        Enumeration<String> attributeNames = request.getAttributeNames();
        if (attributeNames == null) {
            return;
        }

        while (attributeNames.hasMoreElements()) {
            String attributeName = attributeNames.nextElement();
            Object attributeValue = request.getAttribute(attributeName);
            modelAndView.addObject(attributeName, attributeValue);
        }
    }
}
