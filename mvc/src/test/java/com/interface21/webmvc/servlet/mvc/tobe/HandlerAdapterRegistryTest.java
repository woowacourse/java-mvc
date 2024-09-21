package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import com.interface21.webmvc.servlet.mvc.fake.FakeHandlerAdapter;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerAdapterRegistryTest {

    private HandlerAdapterRegistry registry;

    @BeforeEach
    void setUp() {
        registry = new HandlerAdapterRegistry();
        registry.addHandlerAdapter(new FakeHandlerAdapter());
    }

    @DisplayName("핸들러를 지원하는 어댑터가 존재한다.")
    @Test
    void successfulGetAdapter() {
        String handler = "jazz";

        Optional<HandlerAdapter> handlerAdapter = registry.getHandlerAdapter(handler);

        assertThat(handlerAdapter).isPresent();
        assertThat(handlerAdapter.get()).isInstanceOf(FakeHandlerAdapter.class);
    }

    @DisplayName("핸들러를 지원하는 어댑터가 존재하지 않으면 Empty를 반환한다.")
    @Test
    void adapterNotFound() {
        Integer handler = 1;

        Optional<HandlerAdapter> handlerAdapter = registry.getHandlerAdapter(handler);

        assertThat(handlerAdapter).isEmpty();
    }
}
