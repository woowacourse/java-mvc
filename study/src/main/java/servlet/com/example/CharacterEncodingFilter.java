package servlet.com.example;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;

import java.io.IOException;

@WebFilter("/*")
public class CharacterEncodingFilter implements Filter {

    private static final String UTF8 = "UTF-8";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        request.getServletContext().log(String.format("[%s] doFilter() 호출", Thread.currentThread().getName()));

        // 1) 요청 본문 인코딩: 파라미터 읽기 전에 지정해야 효과 있음 (POST 본문)
        if (request.getCharacterEncoding() == null) {
            request.setCharacterEncoding(UTF8);
        }

        // 2) 응답 인코딩: getWriter() 호출 전에 지정해야 함
        response.setCharacterEncoding(UTF8);

        // 3) Content-Type에 charset을 명시 (text/html, application/json 등 상황에 맞게)
        // 이미 설정돼 있지 않다면 기본값을 부여
        if (response.getContentType() == null) {
            response.setContentType("text/html; charset=" + UTF8);
        } else if (!response.getContentType().toLowerCase().contains("charset")) {
            response.setContentType(response.getContentType() + "; charset=" + UTF8);
        }

        chain.doFilter(request, response);
    }
}
