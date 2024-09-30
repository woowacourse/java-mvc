package com.techcourse;

import com.interface21.webmvc.servlet.mvc.tobe.adapter.AnnotationHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.adapter.HandlerAdapters;

public class HandlerAdaptersInitializer {

    public HandlerAdaptersInitializer() {
    }

    public HandlerAdapters initialize() {
        HandlerAdapters handlerAdapters = new HandlerAdapters();
        handlerAdapters.addHandlerAdapter(new AnnotationHandlerAdapter());
        return handlerAdapters;
    }
}
