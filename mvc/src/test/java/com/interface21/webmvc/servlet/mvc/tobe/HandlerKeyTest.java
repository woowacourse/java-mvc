package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.interface21.web.bind.annotation.RequestMethod;

class HandlerKeyTest {

    @Test
    @DisplayName("객체 동등성 검사")
    void equals() {
        //given
        final var getCrew = new HandlerKey("/crew", RequestMethod.GET);
        final var postCrew = new HandlerKey("/crew", RequestMethod.POST);
        final var getCoach = new HandlerKey("/coach", RequestMethod.GET);

        //when && then
        assertThat(getCrew)
                .isEqualTo(new HandlerKey("/crew", RequestMethod.GET))
                .isNotEqualTo(postCrew)
                .isNotEqualTo(getCoach);
    }
}
