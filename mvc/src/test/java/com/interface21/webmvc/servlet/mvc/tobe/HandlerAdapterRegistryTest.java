package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.Mockito.mock;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.view.JspView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestManualHandlerAdapter;

class HandlerAdapterRegistryTest {

    private HandlerAdapterRegistry registry;

    @BeforeEach
    void setUp() {
        registry = new HandlerAdapterRegistry();
        registry.addHandlerAdapter(new AnnotationHandlerAdapter());
        registry.addHandlerAdapter(new TestManualHandlerAdapter());
    }

    @DisplayName("HandlerExecution 인스턴스를 전달하면 AnnotationHandlerAdapter를 반환한다.")
    @Test
    void getAnnotationHandlerAdapter() {
        HandlerExecution handler = mock(HandlerExecution.class);

        assertThat(registry.getHandlerAdapter(handler)).isInstanceOf(AnnotationHandlerAdapter.class);
    }

    @DisplayName("Controller 인스턴스를 전달하면 ManualHandlerAdapter를 반환한다.")
    @Test
    void getManualHandlerAdapter() {
        Controller handler = mock(Controller.class);

        assertThat(registry.getHandlerAdapter(handler)).isInstanceOf(TestManualHandlerAdapter.class);
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
