package com.interface21.webmvc.servlet.mvc.handlerAdapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.interface21.webmvc.servlet.mvc.handlerMapping.HandlerExecution;

class HandlerAdapterRegistryTest {

    private HandlerAdapterRegistry sut;

    @BeforeEach
    void setUp() {
        sut = new HandlerAdapterRegistry();
        sut.addHandlerAdapter(new AnnotationHandlerAdapter());
    }

    @Test
    @DisplayName("처리 가능한 어댑터를 반환한다.")
    void getHandlerAdapter() {
        var handler = mock(HandlerExecution.class);

        var actual = sut.getHandlerAdapter(handler);

        assertThat(actual).isInstanceOf(AnnotationHandlerAdapter.class);
    }


    @Test
    @DisplayName("처리할 수 있는 어댑터가 없으면 예외를 던진다.")
    void getHandlerAdapter_throwsException() {
        var handler = new Object();

        assertThatThrownBy(() -> sut.getHandlerAdapter(handler))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("No handlerAdapter found");
    }
}
