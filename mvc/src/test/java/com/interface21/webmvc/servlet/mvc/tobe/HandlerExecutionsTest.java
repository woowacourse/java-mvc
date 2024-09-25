package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.tobe.handler.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.tobe.handler.HandlerExecutions;
import com.interface21.webmvc.servlet.mvc.tobe.handler.HandlerKey;

@DisplayName("HandlerExecutions 테스트")
class HandlerExecutionsTest {

    @DisplayName("존재하는 HandlerKey를 입력하면 매핑된 HandlerExecution을 담은 Optional을 반환한다.")
    @Test
    void findHandlerExecution() {
        // Given
        final HandlerExecutions handlerExecutions = new HandlerExecutions();
        final HandlerKey handlerKey = new HandlerKey("/hi", RequestMethod.GET);
        final HandlerExecution handlerExecution = new HandlerExecution(null, null);
        handlerExecutions.add(handlerKey, handlerExecution);

        // When
        final Optional<HandlerExecution> findHandlerExecution = handlerExecutions.findHandlerExecution(handlerKey);

        // Then
        assertSoftly(softly -> {
            softly.assertThat(findHandlerExecution).isPresent();
            softly.assertThat(findHandlerExecution.get()).isEqualTo(handlerExecution);
        });
    }

    @DisplayName("존재하지 않는 HandlerKey를 입력하면 빈 Optional을 반환한다.")
    @Test
    void notFindHandlerExecution() {
        // Given
        final HandlerExecutions handlerExecutions = new HandlerExecutions();
        final HandlerKey handlerKey = new HandlerKey("/hi", RequestMethod.GET);

        // When
        final Optional<HandlerExecution> findHandlerExecution = handlerExecutions.findHandlerExecution(handlerKey);

        // Then
        assertThat(findHandlerExecution).isEmpty();
    }
}
