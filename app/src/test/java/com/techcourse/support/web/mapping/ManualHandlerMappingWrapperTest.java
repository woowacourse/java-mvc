package com.techcourse.support.web.mapping;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import jakarta.servlet.http.HttpServletRequest;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ManualHandlerMappingWrapperTest {

    @Test
    void 생성자는_호출하면_ManualHandlerMapping을_초기화한다() {
        assertDoesNotThrow(ManualHandlerMappingWrapper::new);
    }

    @Test
    void initialize_메서드는_호출하면_지정한_초기화_작업을_수행한다() {
        final ManualHandlerMappingWrapper manualHandlerMapping = new ManualHandlerMappingWrapper();
        final HttpServletRequest request = mock(HttpServletRequest.class);
        given(request.getRequestURI()).willReturn("/");

        SoftAssertions.assertSoftly(softAssertions -> {

            softAssertions.assertThatCode(manualHandlerMapping::initialize).doesNotThrowAnyException();
            softAssertions.assertThat(manualHandlerMapping.getHandler(request)).isNotNull();
        });
    }

    @Test
    void getHandler_메서드는_유효한_url을_전달하면_해당_요청을_처리할수있는_핸들러를_반환한다() {
        final ManualHandlerMappingWrapper manualHandlerMapping = new ManualHandlerMappingWrapper();
        manualHandlerMapping.initialize();
        final HttpServletRequest request = mock(HttpServletRequest.class);
        given(request.getRequestURI()).willReturn("/");

        final Object actual = manualHandlerMapping.getHandler(request);

        assertThat(actual).isNotNull();
    }

    @Test
    void getHandler_메서드는_유효하지_않은_url을_전달하면_null을_반환한다() {
        final ManualHandlerMappingWrapper manualHandlerMapping = new ManualHandlerMappingWrapper();
        manualHandlerMapping.initialize();
        final HttpServletRequest request = mock(HttpServletRequest.class);
        given(request.getRequestURI()).willReturn("invalid123");

        final Object actual = manualHandlerMapping.getHandler(request);

        assertThat(actual).isNull();
    }
}
