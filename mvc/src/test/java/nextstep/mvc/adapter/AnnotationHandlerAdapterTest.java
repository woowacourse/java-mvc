package nextstep.mvc.adapter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Method;
import nextstep.mvc.controller.HandlerExecution;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AnnotationHandlerAdapterTest {

    private AnnotationHandlerAdapter annotationHandlerAdapter;

    @BeforeEach
    void setUp() {
        annotationHandlerAdapter = new AnnotationHandlerAdapter();
    }

    @DisplayName("HandlerExecution 클래스만 지원한다.")
    @Test
    void supports() {
        assertTrue(annotationHandlerAdapter.supports(mock(HandlerExecution.class)));
    }

    @DisplayName("HandlerExecution 클래스만 지원한다. - 실패, Object 클래스")
    @Test
    void supportsFailed() {
        assertFalse(annotationHandlerAdapter.supports(mock(Object.class)));
    }
}
