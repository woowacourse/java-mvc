package com.interface21.web.bind.annotation;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class RequestMethodTest {

	@DisplayName("주어진 string 으로 RequestMethod 를 찾는다")
	@ParameterizedTest
	@CsvSource({
			"GET, GET",
			"HEAD, HEAD",
			"POST, POST",
			"PUT, PUT",
			"PATCH, PATCH",
			"DELETE, DELETE",
			"OPTIONS, OPTIONS",
			"TRACE, TRACE"
	})
	void from(String method, RequestMethod expectedRequestMethod) {
		// when
		RequestMethod requestMethod = RequestMethod.from(method);

		// then
		assertThat(requestMethod).isEqualTo(expectedRequestMethod);
	}

	@DisplayName("소문자 입력 시 IllegalArgumentException 발생")
	@ParameterizedTest
	@ValueSource(strings = {"get", "post", "put", "delete", "head", "options", "patch", "trace"})
	void from_lowercase_shouldThrowException(String method) {
		// then
		assertThatThrownBy(() -> RequestMethod.from(method))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("Invalid request method : " + method);
	}

	@DisplayName("존재하지 않는 메소드 입력 시 IllegalArgumentException 발생")
	@Test
	void from_withNotFoundMethod() {
		// given
		String method = "puut";

		// when & then
		assertThatThrownBy(() -> RequestMethod.from(method))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("Invalid request method : " + method);
	}
}
