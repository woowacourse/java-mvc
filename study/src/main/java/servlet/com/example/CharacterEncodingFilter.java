package servlet.com.example;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@WebFilter("/*")
public class CharacterEncodingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        request.getServletContext().log("doFilter() 호출");
        // 방법1)
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        // 방법2)
        //response.setContentType("text/html; charset=UTF-8");

        // 방법3)
        //response.setCharacterEncoding("UTF-8");
        //response.setLocale(Locale.KOREA);
        chain.doFilter(request, response);
    }
}
