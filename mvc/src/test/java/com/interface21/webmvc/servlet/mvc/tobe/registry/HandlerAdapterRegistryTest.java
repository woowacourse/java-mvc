package com.interface21.webmvc.servlet.mvc.tobe.registry;

import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.tobe.adapter.AnnotationHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.adapter.HandlerAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;

class HandlerAdapterRegistryTest {

    @Test
    @DisplayName("Handler Adapter를 캐싱하여 저장후 가져온다.")
    void gatHandlerAdapter() {
        AnnotationHandlerAdapter annotationHandlerAdapter = new AnnotationHandlerAdapter();
        HandlerAdapterRegistry.addHandlerAdapters(Set.of(annotationHandlerAdapter));

        HandlerAdapter handlerAdapter = getMockHandlerAdapter().get();

        assertThat(annotationHandlerAdapter).hasSameHashCodeAs(handlerAdapter);
    }

    @Test
    @DisplayName("Handler Adapter가 없는 경우 새로 저장한다.")
    void addHandlerAdapter_WhenNewHandlerAdapter() {
        Optional<HandlerAdapter> notInsertedHandlerAdapter = getMockHandlerAdapter();

        AnnotationHandlerAdapter annotationHandlerAdapter = new AnnotationHandlerAdapter();
        HandlerAdapterRegistry.addHandlerAdapters(Set.of(annotationHandlerAdapter));

        HandlerAdapter insertedHandlerAdapter = getMockHandlerAdapter().get();

        assertAll(
                () -> assertThat(notInsertedHandlerAdapter).isEmpty(),
                () -> assertThat(insertedHandlerAdapter).hasSameHashCodeAs(annotationHandlerAdapter)
        );
    }

    private static Optional<HandlerAdapter> getMockHandlerAdapter() {
        Object handlerExecution = mock(HandlerExecution.class);
        return HandlerAdapterRegistry.getHandlerAdapter(handlerExecution);
    }
}
