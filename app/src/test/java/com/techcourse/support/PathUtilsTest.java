package com.techcourse.support;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class PathUtilsTest {

    @ParameterizedTest
    @ValueSource(strings = {"/app", ""})
    @DisplayName("webapp path 추가 테스트")
    void addRightWebAppPath(String path) {

        // when
        final String webAppPath = PathUtils.addRightWebAppPath(path);

        // then
        assertThat(webAppPath).isEqualTo("/app/webapp");
    }
}
