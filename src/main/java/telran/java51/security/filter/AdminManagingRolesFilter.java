package telran.java51.security.filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import jakarta.servlet.*;
import telran.java51.accounting.dao.AccountRepository;
import telran.java51.accounting.model.Role;
import telran.java51.accounting.model.User;

import java.io.IOException;
import java.util.Base64;

@Component
@RequiredArgsConstructor
@Order(30)
public class AdminManagingRolesFilter implements Filter {
    final AccountRepository accountRepository;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        if (checkEndPoint(request.getMethod(), request.getServletPath())) {
            User user = accountRepository.findById(request.getUserPrincipal().getName()).get();
            if (!user.getRoles().contains(Role.ADMINISTRATOR)) {
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
