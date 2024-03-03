package telran.java51.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import telran.java51.accounting.dao.AccountRepository;
import telran.java51.accounting.model.User;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userAccount = accountRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        Collection<String> authorities = userAccount.getRoles()
                .stream()
                .map(r -> "ROLE_" + r.name())
                .collect(Collectors.toList());
        return new org.springframework.security.core.userdetails.User(username, userAccount.getPassword(),
                AuthorityUtils.createAuthorityList(authorities));
    }
}
