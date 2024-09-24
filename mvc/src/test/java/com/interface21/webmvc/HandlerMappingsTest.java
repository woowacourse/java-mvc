package com.interface21.webmvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.HandlerMapping;
import com.interface21.webmvc.servlet.NoHandlerFoundException;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerMappingsTest {

    private HandlerMappings handlerMappings = new HandlerMappings();
    private HandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping();

    @BeforeEach
    void setUp() {
        handlerMappings = new HandlerMappings();
        annotationHandlerMapping = new AnnotationHandlerMapping("samples");
    }

    @DisplayName("핸들러 매핑을 넣으면 해당 매핑을 초기화하고 넣는다.")
    @Test
    void givenHandlerMapping_thenAddHandlerMapping() {
        handlerMappings.addHandlerMapping(annotationHandlerMapping);
        List<HandlerMapping> mappings = handlerMappings.getHandlerMappings();

        assertThat(mappings).hasSize(1);
    }

    @DisplayName("핸들러와 매핑되지 않은 요청이 오면 예외가 발생한다.")
    @Test
    void givenInvalidRequest_thenThrowException() {
        handlerMappings.addHandlerMapping(annotationHandlerMapping);

        assertThatThrownBy(() -> handlerMappings.getHandlerMapping("/invalid", RequestMethod.DELETE))
                .isInstanceOf(NoHandlerFoundException.class)
                .hasMessage("[DELETE /invalid]에 매핑된 핸들러가 존재하지 않습니다.");
    }

    @DisplayName("핸들러와 매핑된 요청을 보내면 url에 맞는 핸들러 매핑을 반환한다.")
    @Test
    void givenExistRequest_thenReturnHandler() {
        handlerMappings.addHandlerMapping(annotationHandlerMapping);

        HandlerMapping handlerMapping = handlerMappings.getHandlerMapping("/get-test", RequestMethod.GET);

        assertAll(
                () -> assertThatCode(() -> handlerMappings.getHandlerMapping("/get-test", RequestMethod.GET))
                        .doesNotThrowAnyException(),
                () -> assertThat(handlerMapping).isExactlyInstanceOf(AnnotationHandlerMapping.class)
        );
    }
}
