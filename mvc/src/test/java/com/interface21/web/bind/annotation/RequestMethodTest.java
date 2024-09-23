package com.interface21.web.bind.annotation;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class RequestMethodTest {

	@DisplayName("주어진 string 으로 RequestMethod 를 찾는다")
	@ParameterizedTest
	@ValueSource(strings = {"get", "GET", "Get"})
	void from(String method) {
		// when
		RequestMethod requestMethod = RequestMethod.from(method);

		// then
		assertThat(requestMethod).isEqualTo(RequestMethod.GET);
	}

	@DisplayName("주어진 string 으로 RequestMethod 를 찾는다")
	@Test
	void from_withNotFoundMethod() {
		// given
		String method = "puut";

		// when & then
		assertThatThrownBy(() -> RequestMethod.from(method))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("not fount request method : " + method);
	}
}
