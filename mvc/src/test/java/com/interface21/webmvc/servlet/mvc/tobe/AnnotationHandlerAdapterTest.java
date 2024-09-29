package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AnnotationHandlerAdapterTest {

    @DisplayName("지원하는 Handler인지 확인할 수 있다: true")
    @Test
    void isSupportedTrue() {
        // given
        AnnotationHandlerAdapter annotationHandlerAdapter = new AnnotationHandlerAdapter();

        // when
        boolean isSupported = annotationHandlerAdapter.isSupported(new HandlerExecution(null, null));

        // then
        assertThat(isSupported).isTrue();
    }

    @DisplayName("지원하는 Handler인지 확인할 수 있다: false")
    @Test
    void isSupportedFalse() {
        // given
        AnnotationHandlerAdapter annotationHandlerAdapter = new AnnotationHandlerAdapter();

        // when
        boolean isSupported = annotationHandlerAdapter.isSupported(new String());

        // then
        assertThat(isSupported).isFalse();
    }
}
