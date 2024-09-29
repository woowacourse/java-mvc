package com.interface21.webmvc.servlet.mvc.tobe.registry;

import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.tobe.handlermapping.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.pathfinder.RootPathFinder;
import com.interface21.webmvc.servlet.mvc.tobe.pathfinder.TestRootPathStrategy;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HandlerMappingRegistryTest {

    @Test
    @DisplayName("HandlerMapping을 캐싱하여 저장한 후 이용하여 Handler를 가져온다.")
    void addHandlerMappings_WhenAnnotationHandlerMapping() {
        RootPathFinder rootPathFinder = new RootPathFinder(new TestRootPathStrategy());
        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping(rootPathFinder);

        annotationHandlerMapping.initialize();

        HandlerMappingRegistry.addHandlerMappings(Set.of(annotationHandlerMapping));

        assertThat(getMockHandlerMapping().get()).isInstanceOf(HandlerExecution.class);
    }

    private static Optional<Object> getMockHandlerMapping() {
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        return HandlerMappingRegistry.getHandler(request);
    }
}
