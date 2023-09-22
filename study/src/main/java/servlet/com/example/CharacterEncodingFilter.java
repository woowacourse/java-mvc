package servlet.com.example;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;

import java.io.IOException;

@WebFilter("/*")
public class CharacterEncodingFilter implements Filter {

    public static final String UTF_8 = "UTF-8";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        request.getServletContext().log("doFilter() 호출");
        request.setCharacterEncoding(UTF_8);
        response.setCharacterEncoding(UTF_8);
        chain.doFilter(request, response);
    }
}
