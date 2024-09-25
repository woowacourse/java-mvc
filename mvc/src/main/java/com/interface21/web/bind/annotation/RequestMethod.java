package com.interface21.web.bind.annotation;

import java.util.Arrays;

public enum RequestMethod {
	GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;

	public static RequestMethod from(String method) {
		return Arrays.stream(values())
			.filter(requestMethod -> requestMethod.name().equals(method))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("Invalid request method : " + method));
	}
}
