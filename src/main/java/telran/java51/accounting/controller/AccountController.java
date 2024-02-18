package telran.java51.accounting.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import telran.java51.accounting.dto.account.RoleDto;
import telran.java51.accounting.dto.account.UserDto;
import telran.java51.accounting.dto.account.UserRegisterDto;
import telran.java51.accounting.dto.account.UserUpdateDto;
import telran.java51.accounting.service.AccountService;


@RequiredArgsConstructor
@RestController
@RequestMapping("/account")
public class AccountController {
   final AccountService accountService;

    @PostMapping("/register")
    public UserDto register(@RequestBody UserRegisterDto userRegisterDto) {
        return accountService.registerUser(userRegisterDto);
    }

    @PostMapping("/login")
    public UserDto login() {
        return null;
    }

    @DeleteMapping("/user/{user}")
    public UserDto deleteUser(@PathVariable("user") String login) {
        return accountService.deleteUser(login);
    }

    @PutMapping("/user/{user}")
    public UserDto updateUser(@PathVariable("user") String login, @RequestBody UserUpdateDto userUpdateDto) {
        return accountService.updateUser(login, userUpdateDto);
    }

    @PutMapping("/user/{user}/role/{role}")
    public RoleDto addRole(@PathVariable("user") String login, @PathVariable String role) {
        return accountService.addRole(login, role);
    }

    @DeleteMapping("/user/{user}/role/{role}")
    public RoleDto deleteRole(@PathVariable("user") String login, @PathVariable String role) {
        return accountService.deleteRole(login, role);
    }

    @GetMapping("/user/{user}")
    public UserDto getUser(@PathVariable("user") String login) {
        return accountService.getUser(login);
    }

}
