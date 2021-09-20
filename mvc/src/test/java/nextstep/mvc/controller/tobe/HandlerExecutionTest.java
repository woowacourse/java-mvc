package nextstep.mvc.controller.tobe;

import nextstep.mvc.exeption.HandlerAdapterException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.*;

@DisplayName("HandlerExecution 예외 처리 테스트")
class HandlerExecutionTest {

    private static Stream<Arguments> exceptionTest() {
        return Stream.of(
                Arguments.of(IllegalAccessException.class),
                Arguments.of(IllegalArgumentException.class),
                Arguments.of(NullPointerException.class)
        );
    }

    @ParameterizedTest
    @MethodSource(value = "exceptionTest")
    void exception_test(Class<? extends Exception> exception) throws InvocationTargetException, IllegalAccessException {
        //given
        final Method mockMethod = mock(Method.class);
        final Object mockHandler = mock(Object.class);
        final HandlerExecution handlerExecution = new HandlerExecution(mockHandler, mockMethod);
        //when
        willReturn(new Class[]{}).given(mockMethod).getParameterTypes();
        willThrow(exception).given(mockMethod).invoke(any(), any());
        //then
        Assertions.assertThatThrownBy(handlerExecution::handle)
                .isInstanceOf(HandlerAdapterException.class);
    }

    @Test
    void exception_when_invoke() throws InvocationTargetException, IllegalAccessException {
        //given
        final Method mockMethod = mock(Method.class);
        final Object mockHandler = mock(Object.class);
        final InvocationTargetException mockException = spy(InvocationTargetException.class);
        final HandlerExecution handlerExecution = new HandlerExecution(mockHandler, mockMethod);
        //when
        willReturn(new Class[]{}).given(mockMethod).getParameterTypes();
        when(mockException.getTargetException()).thenReturn(new RuntimeException());
        willThrow(mockException).given(mockMethod).invoke(any(), any());
        //then
        Assertions.assertThatThrownBy(handlerExecution::handle)
                .isInstanceOf(HandlerAdapterException.class);
    }
}
