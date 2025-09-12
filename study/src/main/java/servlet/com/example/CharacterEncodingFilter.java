package servlet.com.example;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;

import java.io.IOException;

@WebFilter("/*")
public class CharacterEncodingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        request.getServletContext().log("doFilter() 호출");
        response.setContentType("text/html; charset=UTF-8");     // 브라우저 인코딩도 처리
//        response.setCharacterEncoding("UTF-8");
        chain.doFilter(request, response);
    }
}
