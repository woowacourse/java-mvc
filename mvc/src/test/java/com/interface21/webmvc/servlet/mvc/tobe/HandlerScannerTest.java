package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import org.junit.jupiter.api.Test;
import samples.TestController;
import samples.TestController1;
import samples.TestController2;

class HandlerScannerTest {

    @Test
    void 패키지에_존재하는_handler_객체들을_생성한다() {
        // given
        Object[] basePackages = new String[]{"samples"};
        HandlerScanner handlerScanner = new HandlerScanner(basePackages);

        // when
        Map<Class<?>, Object> handlers = handlerScanner.getHandlers();

        // then
        assertSoftly(softly -> {
            softly.assertThat(handlers).hasSize(3);
            softly.assertThat(handlers.get(TestController.class)).isExactlyInstanceOf(TestController.class);
            softly.assertThat(handlers.get(TestController1.class)).isExactlyInstanceOf(TestController1.class);
            softly.assertThat(handlers.get(TestController2.class)).isExactlyInstanceOf(TestController2.class);
        });
    }

    @Test
    void handler가_추상클래스일_경우_HandlerCreationException이_발생한다() {
        // given
        Object[] basePackages = new String[]{"exception.instantiation"};
        HandlerScanner handlerScanner = new HandlerScanner(basePackages);

        // when & then
        assertThatThrownBy(handlerScanner::getHandlers)
                .isExactlyInstanceOf(HandlerCreationException.class)
                .hasCauseExactlyInstanceOf(InstantiationException.class)
                .hasMessage("Error creating handler with name 'exception.instantiation.InstantiationExceptionController'");
    }

    @Test
    void handler에_기본_생성자가_없을_경우_HandlerCreationException이_발생한다() {
        // given
        Object[] basePackages = new String[]{"exception.nosuchmethod"};
        HandlerScanner handlerScanner = new HandlerScanner(basePackages);

        // when & then
        assertThatThrownBy(handlerScanner::getHandlers)
                .isExactlyInstanceOf(HandlerCreationException.class)
                .hasCauseExactlyInstanceOf(NoSuchMethodException.class)
                .hasMessage("Error creating handler with name 'exception.nosuchmethod.NoSuchMethodExceptionController'");
    }

    @Test
    void handler_생성자의_접근제어자가_public이_아닐_경우_HandlerCreationException이_발생한다() {
        // given
        Object[] basePackages = new String[]{"exception.illegalaccess"};
        HandlerScanner handlerScanner = new HandlerScanner(basePackages);

        // when & then
        assertThatThrownBy(handlerScanner::getHandlers)
                .isExactlyInstanceOf(HandlerCreationException.class)
                .hasCauseExactlyInstanceOf(IllegalAccessException.class)
                .hasMessage("Error creating handler with name 'exception.illegalaccess.IllegalAccessExceptionController'");
    }

    @Test
    void handler_생성자에서_예외가_발생할_경우_HandlerCreationException이_발생한다() {
        // given
        Object[] basePackages = new String[]{"exception.invocationtarget"};
        HandlerScanner handlerScanner = new HandlerScanner(basePackages);

        // when & then
        assertThatThrownBy(handlerScanner::getHandlers)
                .isExactlyInstanceOf(HandlerCreationException.class)
                .hasCauseExactlyInstanceOf(InvocationTargetException.class)
                .hasMessage("Error creating handler with name 'exception.invocationtarget.InvocationTargetExceptionController'");
    }
}
