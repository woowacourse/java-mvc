package servlet.com.example;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebFilter("/*")
public class CharacterEncodingFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(CharacterEncodingFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        request.getServletContext().log("doFilter() 호출");
        log.info("기존 characterEncoding = {}", response.getCharacterEncoding());
        response.setCharacterEncoding("UTF-8");
        log.info("변경 characterEncoding = {}", response.getCharacterEncoding());
        chain.doFilter(request, response);
    }
}
