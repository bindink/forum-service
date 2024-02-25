package telran.java51.security.filter;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import telran.java51.security.filter.utils.FilterUtils;

import java.io.IOException;

import static telran.java51.security.filter.utils.FilterUtils.ERROR_403_RESPONSE_TEXT;

@Component
@Order(40)
public class AddPostByAuthorFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        if (checkEndPoint(request.getMethod(), request.getServletPath())) {
            if (!FilterUtils.isRequestPathUserAuthenticated(request)) {
                response.sendError(403, ERROR_403_RESPONSE_TEXT);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private boolean checkEndPoint(String method, String path) {
        return HttpMethod.POST.matches(method) && path.matches("/forum/post/\\w+");
    }
}
