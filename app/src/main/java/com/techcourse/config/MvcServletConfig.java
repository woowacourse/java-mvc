package com.techcourse.config;

import com.interface21.webmvc.servlet.adapter.AnnotationHandlerAdapter;
import com.interface21.webmvc.servlet.adapter.HandlerAdapterRegistry;
import com.interface21.webmvc.servlet.adapter.ManualHandlerAdapter;
import com.interface21.webmvc.servlet.mapping.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mapping.HandlerMapping;
import com.interface21.webmvc.servlet.mapping.HandlerMappingRegistry;
import com.techcourse.ManualHandlerMapping;
import java.util.List;

public class MvcServletConfig {

        public HandlerAdapterRegistry createHandlerAdapterRegistry() {
            return new HandlerAdapterRegistry(
                    List.of(
                            new ManualHandlerAdapter(),
                            new AnnotationHandlerAdapter()
                    )
            );
        }

        public HandlerMappingRegistry createHandlerMappingRegistry() {
            return new HandlerMappingRegistry(
                    List.of(
                            manualHandlerMapping(),
                            annotationHandlerMapping()
                    )
            );
        }

        public HandlerMapping manualHandlerMapping() {
            ManualHandlerMapping mapping = new ManualHandlerMapping();
            mapping.initialize();
            return mapping;
        }

        public HandlerMapping annotationHandlerMapping() {
            AnnotationHandlerMapping mapping = new AnnotationHandlerMapping();
            mapping.initialize();
            return mapping;
        }
}
