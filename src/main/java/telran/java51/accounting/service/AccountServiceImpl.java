package telran.java51.accounting.service;

import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import telran.java51.accounting.dao.AccountRepository;
import telran.java51.accounting.dto.account.RoleDto;
import telran.java51.accounting.dto.account.UserDto;
import telran.java51.accounting.dto.account.UserRegisterDto;
import telran.java51.accounting.dto.account.UserUpdateDto;
import telran.java51.accounting.dto.exceptions.UserExistsException;
import telran.java51.accounting.dto.exceptions.UserNotFoundException;
import telran.java51.accounting.model.Role;
import telran.java51.accounting.model.User;

import java.util.Arrays;
import java.util.HashSet;

import static com.fasterxml.jackson.databind.type.LogicalType.Collection;

@RequiredArgsConstructor
@Service
public class AccountServiceImpl implements AccountService, CommandLineRunner {
    final AccountRepository accountRepository;
    final ModelMapper modelMapper;

    @Override
    public UserDto registerUser(UserRegisterDto userRegisterDto) {
        if (accountRepository.existsById(userRegisterDto.getLogin())) {
            throw new UserExistsException();
        }
        User user = modelMapper.map(userRegisterDto, User.class);
        String password = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(password);
        user.addRole(Role.USER);
        accountRepository.save(user);
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public UserDto deleteUser(String login) {
        User user = accountRepository.findById(login).orElseThrow(UserNotFoundException::new);
        accountRepository.deleteById(login);
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public UserDto updateUser(String login, UserUpdateDto userUpdateDto) {
        User user = accountRepository.findById(login).orElseThrow(UserNotFoundException::new);
        if (userUpdateDto.getFirstName() != null) {
            user.setFirstName(userUpdateDto.getFirstName());
        }

        if (userUpdateDto.getLastName() != null) {
            user.setLastName(userUpdateDto.getLastName());
        }
        accountRepository.save(user);
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public RoleDto addRole(String login, String role) {
        User user = accountRepository.findById(login).orElseThrow(UserNotFoundException::new);
        user.addRole(Role.valueOf(role.toUpperCase()));
        accountRepository.save(user);
        return modelMapper.map(user, RoleDto.class);
    }

    @Override
    public RoleDto deleteRole(String login, String role) {
        User user = accountRepository.findById(login).orElseThrow(UserNotFoundException::new);
        if (!Role.USER.toString().equalsIgnoreCase(role)) {
            user.removeRole(Role.valueOf(role.toUpperCase()));
            accountRepository.save(user);
        }
        return modelMapper.map(user, RoleDto.class);
    }

    @Override
    public UserDto getUser(String login) {
        User user = accountRepository.findById(login).orElseThrow(UserNotFoundException::new);
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public void changePassword(String login, String newPassword) {
        User user = accountRepository.findById(login).orElseThrow(UserNotFoundException::new);
        String password = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        user.setPassword(password);
        accountRepository.save(user);
    }

    @Override
    public void run(String... args) throws Exception {
        if (!accountRepository.existsById("admin")) {
            String password = BCrypt.hashpw("admin", BCrypt.gensalt());
            User admin = new User("admin", "", "", password, new HashSet<>(Arrays.asList(Role.MODERATOR, Role.ADMINISTRATOR )));
            accountRepository.save(admin);
        }
    }
}
