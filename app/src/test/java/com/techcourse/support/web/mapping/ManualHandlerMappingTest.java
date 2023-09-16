package com.techcourse.support.web.mapping;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ManualHandlerMappingTest {

    @Test
    void 생성자는_호출하면_ManualHandlerMapping을_초기화한다() {
        assertDoesNotThrow(ManualHandlerMapping::new);
    }

    @Test
    void initialize_메서드는_호출하면_지정한_초기화_작업을_수행한다() {
        final ManualHandlerMapping manualHandlerMapping = new ManualHandlerMapping();

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThatCode(manualHandlerMapping::initialize).doesNotThrowAnyException();
            softAssertions.assertThat(manualHandlerMapping.getHandler("/")).isNotNull();
        });
    }

    @Test
    void getHandler_메서드는_유효한_url을_전달하면_해당_요청을_처리할수있는_핸들러를_반환한다() {
        final ManualHandlerMapping manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();

        final Controller actual = manualHandlerMapping.getHandler("/login");

        assertThat(actual).isNotNull();
    }

    @Test
    void getHandler_메서드는_유효하지_않은_url을_전달하면_null을_반환한다() {
        final String invalidUrl = "invalid123";
        final ManualHandlerMapping manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();

        final Controller actual = manualHandlerMapping.getHandler(invalidUrl);

        assertThat(actual).isNull();
    }
}
