package servlet.com.example;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;

import java.io.IOException;

@WebFilter("/*")
public class CharacterEncodingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        request.getServletContext().log("doFilter() 호출");

        // CharacterEncodingFilter에서 response에 UTF-8 설정을 넣어줌.
        response.setContentType("text/html;charset=utf-8");
        chain.doFilter(request, response);
    }
}
