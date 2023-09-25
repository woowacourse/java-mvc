package web.org.springframework.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface Handler {

    Object handle(HttpServletRequest request, HttpServletResponse response) throws Exception;
}
