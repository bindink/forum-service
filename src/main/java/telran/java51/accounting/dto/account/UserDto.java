package telran.java51.accounting.dto.account;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Getter
@NoArgsConstructor
public class UserDto {
     String login;
     String firstName;
     String lastName;
     ArrayList<String> roles = new ArrayList<>();
}
