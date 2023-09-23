package com.techcourse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ManualHandlerAdapterTest {

    @Test
    void 매뉴얼_핸들러_어답터는_Controller를_지원한다() {
        // given
        final Controller controller = mock(Controller.class);
        final ManualHandlerAdapter manualHandlerAdapter = new ManualHandlerAdapter();

        // expected
        assertThat(manualHandlerAdapter.isSupported(controller)).isTrue();
    }
}
