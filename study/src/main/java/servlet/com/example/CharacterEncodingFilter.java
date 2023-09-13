package servlet.com.example;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;

import java.io.IOException;

@WebFilter("/*")
public class CharacterEncodingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        request.getServletContext().log("doFilter() 호출");

        final String body = "인코딩";
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        response.setContentLength(body.getBytes().length);
        response.getWriter().write(body);

        chain.doFilter(request, response);
    }
}
