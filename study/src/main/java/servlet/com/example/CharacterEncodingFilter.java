package servlet.com.example;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Locale.KOREA;

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

        response.setCharacterEncoding(UTF_8.name());
        response.setLocale(KOREA);

        chain.doFilter(request, response);
    }
}
