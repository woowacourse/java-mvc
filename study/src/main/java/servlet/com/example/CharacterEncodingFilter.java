package servlet.com.example;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;

import java.io.IOException;
import java.util.Locale;

import org.apache.tomcat.util.buf.Utf8Encoder;

@WebFilter("/*")
public class CharacterEncodingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        request.getServletContext().log("doFilter() 호출");
        response.setLocale(Locale.KOREAN);
        chain.doFilter(request, response);
    }
}
