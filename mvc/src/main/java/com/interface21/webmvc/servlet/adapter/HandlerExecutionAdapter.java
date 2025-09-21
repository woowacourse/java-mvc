package com.interface21.webmvc.servlet.adapter;

import com.interface21.webmvc.servlet.Handler;
import com.interface21.webmvc.servlet.HandlerType;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HandlerExecutionAdapter extends AbstractHandlerAdapter {

    @Override
    protected HandlerType supportedType() {
        return HandlerType.HANDLER_EXECUTION;
    }

    @Override
    protected ModelAndView doHandle(
            final Handler handler,
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws Exception {
        return ((HandlerExecution) handler.instance()).handle(request, response);
    }
}
