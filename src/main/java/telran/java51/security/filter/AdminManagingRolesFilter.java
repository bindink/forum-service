package telran.java51.security.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import telran.java51.security.model.UserPrincipal;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Order(30)
public class AdminManagingRolesFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        if (checkEndPoint(request.getMethod(), request.getServletPath())) {
            UserPrincipal user = (UserPrincipal)request.getUserPrincipal();
            if (!user.getRoles().contains("ADMINISTRATOR")) {
                response.sendError(403, "Permission denied");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }


    private boolean checkEndPoint(String method, String path) {
        return path.matches("/account/user/\\w+/role/\\w+");
    }

}
