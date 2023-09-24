package webmvc.org.springframework.web.servlet.view;

import static webmvc.org.springframework.web.servlet.view.ViewType.JSP;

import webmvc.org.springframework.web.servlet.View;

public class ViewAdapter {

    public View getView(String viewName) {
        ViewType viewType = negotiateViewType(viewName);
        if (viewType.equals(JSP)) {
            return new JspView(viewName);
        }
        return new JsonView();
    }

    private ViewType negotiateViewType(String viewName) {
        int index = viewName.indexOf('.');
        if (index == -1) {
            throw new RuntimeException("확장자명이 없습니다.");
        }

        return ViewType.from(viewName.substring(index + 1));
    }
}
