package servlet.com.example;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;

import java.io.IOException;

@WebFilter("/*")
public class CharacterEncodingFilter implements Filter {

    private static final String DEFAULT_ENCODING = "UTF-8";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        request.getServletContext().log("doFilter() 호출");
        request.setCharacterEncoding(DEFAULT_ENCODING);
        response. setCharacterEncoding(DEFAULT_ENCODING);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain; charset=UTF-8");
        chain.doFilter(request, response);
    }
}
