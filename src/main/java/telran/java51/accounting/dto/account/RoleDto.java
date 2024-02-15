package telran.java51.accounting.dto.account;

import lombok.Getter;
import lombok.NoArgsConstructor;
import telran.java51.accounting.model.Role;

import java.util.ArrayList;

@Getter
@NoArgsConstructor
public class RoleDto {
    String login;
    ArrayList<Role> roles;
}
