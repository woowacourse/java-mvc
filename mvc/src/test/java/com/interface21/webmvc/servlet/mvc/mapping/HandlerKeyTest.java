package com.interface21.webmvc.servlet.mvc.mapping;

import static org.assertj.core.api.Assertions.assertThat;

import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

class HandlerKeyTest {

    @DisplayName("HttpServletRequest로부터 HandlerKey를 생성한다.")
    @Test
    void createFromHttpServletRequest() {
        // given
        HttpServletRequest request = new MockHttpServletRequest("GET", "/test");

        // when
        HandlerKey actual = HandlerKey.from(request);

        // then
        HandlerKey expected = new HandlerKey("/test", RequestMethod.GET);
        assertThat(actual).isEqualTo(expected);
    }
}
