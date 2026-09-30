package com.interface21.webmvc.servlet.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.Test;

class HandlerAdapterRegistryTest {

    @Test
    void 핸들러를_지원하는_어댑터를_반환한다() {
        Object handler = new Object();
        HandlerAdapter unsupported = mock(HandlerAdapter.class);
        HandlerAdapter supported = mock(HandlerAdapter.class);
        when(supported.supports(handler)).thenReturn(true);
        HandlerAdapterRegistry registry =
                new HandlerAdapterRegistry(List.of(unsupported, supported));

        assertThat(registry.getHandlerAdapter(handler)).isSameAs(supported);
    }

    @Test
    void 핸들러를_지원하는_어댑터가_없으면_예외가_발생한다() {
        HandlerAdapterRegistry registry = new HandlerAdapterRegistry(List.of());

        assertThatIllegalArgumentException()
                .isThrownBy(() -> registry.getHandlerAdapter(new Object()))
                .withMessageContaining("어댑터가 없습니다");
    }
}
