package telran.java51.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import telran.java51.accounting.model.Role;

@Configuration
@RequiredArgsConstructor
public class AuthorizationConfiguration {
    final UpdatePostCustomAuthorizationManager updatePostAuthManager;
    final DeletePostCustomAuthorizationManager deletePostAuthManager;
    @Bean
    public SecurityFilterChain web(HttpSecurity http) throws Exception {
        http.httpBasic(Customizer.withDefaults());
        http.csrf(csrf -> csrf.disable());
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS));
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/account/register", "/forum/posts/**")
                    .permitAll()
                .requestMatchers("/account/user/{login}/role/{role}")// .requestMatchers("/account/user/{login}/role/{role:moderator|administrator}")
                    .hasRole(Role.ADMINISTRATOR.name())
                .requestMatchers(HttpMethod.PUT,"/account/user/{login}")
                    .access(new WebExpressionAuthorizationManager("#login == authentication.name"))
                .requestMatchers(HttpMethod.DELETE,"/account/user/{login}")
                    .access(new WebExpressionAuthorizationManager("#login == authentication.name or hasRole('ADMINISTRATOR')" ))
                .requestMatchers(HttpMethod.POST,"/forum/post/{author}")
                    .access(new WebExpressionAuthorizationManager("#author == authentication.name"))
                .requestMatchers(HttpMethod.POST,"/forum/post/{id}/comment/{author}")
                    .access(new WebExpressionAuthorizationManager("#author == authentication.name"))
                .requestMatchers(HttpMethod.PUT,"/forum/post/{id}")
//                    .access(new WebExpressionAuthorizationManager("@customSecurity.check(#id, authentication.name)"))
                .access(updatePostAuthManager)
                .requestMatchers(HttpMethod.DELETE,"/forum/post/{id}")
                .access(deletePostAuthManager)
                .anyRequest()
                    .authenticated());
        return http.build();
    }
}