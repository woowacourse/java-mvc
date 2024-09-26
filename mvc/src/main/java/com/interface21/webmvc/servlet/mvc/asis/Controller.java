package com.interface21.webmvc.servlet.mvc.asis;

import com.interface21.webmvc.servlet.AbstractView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface Controller {
    String execute(final HttpServletRequest req, final HttpServletResponse res) throws Exception;

    default Class<? extends AbstractView> getSupportViewClass() {
        return JspView.class;
    }
}
