package telran.java51.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Service;
import telran.java51.post.dao.PostRepository;
import telran.java51.post.model.Post;

import java.util.function.Supplier;

@Service("deletePostAuthManager")
@RequiredArgsConstructor
public class DeletePostCustomAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {
    final PostRepository postRepository;

    @Override
    public AuthorizationDecision check(Supplier authentication, RequestAuthorizationContext authorizationContext) {
        String userName = authorizationContext.getRequest().getUserPrincipal().getName();
        String postId = authorizationContext.getVariables().get("id");
        Post post = postRepository.findById(postId).orElse(null);
        boolean isModerator = authorizationContext.getRequest().isUserInRole("MODERATOR");
        return new AuthorizationDecision (post != null && (userName.equals(post.getAuthor()) || isModerator));
    }
}
