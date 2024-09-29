package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.Mockito.mock;

import com.interface21.webmvc.servlet.mvc.AnnotationHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.HandlerAdapterRegistry;
import com.interface21.webmvc.servlet.mvc.HandlerExecution;
import com.interface21.webmvc.servlet.view.JspView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerAdapterRegistryTest {

    private HandlerAdapterRegistry registry;

    @BeforeEach
    void setUp() {
        registry = new HandlerAdapterRegistry();
        registry.addHandlerAdapter(new AnnotationHandlerAdapter());
    }

    @DisplayName("HandlerExecution 인스턴스를 전달하면 AnnotationHandlerAdapter를 반환한다.")
    @Test
    void getAnnotationHandlerAdapter() {
        HandlerExecution handler = mock(HandlerExecution.class);

        assertThat(registry.getHandlerAdapter(handler)).isInstanceOf(AnnotationHandlerAdapter.class);
    }

    @DisplayName("적합한 핸들러 어댑터가 없으면 예외가 발생한다.")
    @Test
    void getHandlerAdapterWhenNotExist() {
        JspView handler = mock(JspView.class);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> registry.getHandlerAdapter(handler))
                .withMessageContaining("해당 핸들러를 지원하는 핸들러 어댑터가 없습니다. 핸들러: ");
    }
}
