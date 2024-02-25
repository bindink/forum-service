package telran.java51.security.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import telran.java51.post.dao.PostRepository;
import telran.java51.post.model.Post;
import telran.java51.security.model.UserPrincipal;

import java.io.IOException;

import static telran.java51.security.filter.utils.FilterUtils.ERROR_403_RESPONSE_TEXT;
import static telran.java51.security.filter.utils.FilterUtils.ERROR_POST_NOT_FOUND_RESPONSE_TEXT;

@Component
@Order(41)
@RequiredArgsConstructor
public class DeletePostByOwnerOrModeratorFilter implements Filter {
    final PostRepository postRepository;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        if (checkEndPoint(request.getMethod(), request.getServletPath())) {
            try {
                UserPrincipal user = (UserPrincipal) request.getUserPrincipal();
                String[] arr = request.getServletPath().split("/");
                String postId = arr[arr.length - 1];
                Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException(ERROR_POST_NOT_FOUND_RESPONSE_TEXT));
                if (!(user.getRoles().contains("MODERATOR") || user.getName().equals(post.getAuthor()))) {
                    throw new RuntimeException(ERROR_403_RESPONSE_TEXT);
                }
            } catch (Exception e) {
                response.sendError(403, e.getMessage());
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private boolean checkEndPoint(String method, String path) {
        return HttpMethod.DELETE.matches(method) && path.matches("/forum/post/\\w+");
    }
}
