package com.interface21.webmvc.servlet.mvc;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ViewRendererImpl implements ViewRenderer {

    @Override
    public void render(final ModelAndView modelAndView, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        if (modelAndView != null) {
            modelAndView.getView().render(modelAndView.getModel(), request, response);
        }
    }
}
