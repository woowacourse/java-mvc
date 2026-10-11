package com.interface21.webmvc.servlet.handler;

import com.interface21.core.Ordered;
import com.interface21.webmvc.servlet.HandlerMapping;

public abstract class AbstractHandlerMapping implements HandlerMapping, Ordered {

    private int order = Ordered.LOWEST_PRECEDENCE;

    /** Configure priority before constructing the DispatcherServlet. */
    public void setOrder(final int order) {
        this.order = order;
    }

    @Override
    public int getOrder() {
        return order;
    }
}
