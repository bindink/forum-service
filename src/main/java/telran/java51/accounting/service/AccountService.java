package telran.java51.accounting.service;

import telran.java51.accounting.dto.account.RoleDto;
import telran.java51.accounting.dto.account.UserDto;
import telran.java51.accounting.dto.account.UserRegisterDto;
import telran.java51.accounting.dto.account.UserUpdateDto;

public interface AccountService {
    public UserDto registerUser(UserRegisterDto userRegisterDto);

    public UserDto deleteUser (String login);

    public UserDto updateUser(String login, UserUpdateDto userUpdateDto);

    public RoleDto addRole(String login, String role);

    public RoleDto deleteRole(String login, String role);

    public UserDto getUser(String login);
}
