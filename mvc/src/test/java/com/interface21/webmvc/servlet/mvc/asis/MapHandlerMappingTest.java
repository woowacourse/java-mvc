package com.interface21.webmvc.servlet.mvc.asis;

import com.interface21.webmvc.servlet.mvc.HandlerKeys;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerKey;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import samples.PotatoController;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class MapHandlerMappingTest {
    @Test
    void some() {
        final MapHandlerMapping handlerMapping = new MapHandlerMapping(new HandlerKeys(),
                Map.of(new HandlerKey("/potato"), new PotatoController()));
        handlerMapping.initialize();

        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/potato");
        request.setMethod("GET");

        final HandlerExecution execution = handlerMapping.getHandler(new HandlerKey(request));

        final var result = execution.handle(request,new MockHttpServletResponse());

        assertThat(result).isEqualTo("Hi Potato!");
    }
}
