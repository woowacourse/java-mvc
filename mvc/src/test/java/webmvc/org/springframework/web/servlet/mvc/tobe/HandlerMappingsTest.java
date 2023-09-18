package webmvc.org.springframework.web.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import jakarta.servlet.http.HttpServletRequest;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class HandlerMappingsTest {

	private String handler;
	private HttpServletRequest request;

	@BeforeEach
	void setUp() {
		handler = "handler";
		request = Mockito.mock(HttpServletRequest.class);
	}

	@Test
	void 올바른_Handler를_반환한다() {
		// given
		HandlerMapping trueHandlerMapping = createHandlerMapping(handler);
		HandlerMapping falseHandlerMapping = createHandlerMapping(null);
		HandlerMappings handlerMappings = new HandlerMappings(trueHandlerMapping, falseHandlerMapping);

		// when
		Object actual = handlerMappings.getHandler(request);

		// then
		assertThat(actual).isEqualTo(handler);
	}

	private HandlerMapping createHandlerMapping(final String handler) {
		HandlerMapping handlerMapping = Mockito.mock(HandlerMapping.class);
		given(handlerMapping.getHandler(request))
			.willReturn(handler);
		return handlerMapping;
	}

	@Test
	void 올바른_Handler가_없을때_예외() {
		// given
		HandlerMapping falseHandlerMapping1 = createHandlerMapping(null);
		HandlerMapping falseHandlerMapping2 = createHandlerMapping(null);
		HandlerMappings handlerMappings = new HandlerMappings(falseHandlerMapping1, falseHandlerMapping2);

		// when & then
		assertThatThrownBy(() -> handlerMappings.getHandler(request))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("요청에 맞는 Hanlder가 없습니다.");
	}
}
