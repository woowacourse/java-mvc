package com.interface21.webmvc.servlet.view;

public interface JspViewRenderer {

    default ModelAndView showPage(final String viewName) {
        return new ModelAndView(new JspView(viewName));
    }

    default ModelAndView redirect(final String viewName) {
        return new ModelAndView(new JspView("redirect:" + viewName));
    }
}
