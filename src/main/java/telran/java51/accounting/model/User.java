package telran.java51.accounting.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Document(collection = "accounts")
public class User {
    @Id
    String login;
    @Setter
    String firstName;
    @Setter
    String lastName;
    @Setter
    @Getter
    String password;

    @Getter
    Set<Role> roles = new HashSet<>();

    public void addRole(String role) {
        roles.add(Role.valueOf(role.toUpperCase()));
    }

    public void removeRole(String role) {
        roles.remove(Role.valueOf(role.toUpperCase()));
    }

    public User() {
        roles = new HashSet<>();
        addRole("USER");
    }
    public User(String login, String password, String firstName, String lastName) {
        this();
        this.login = login;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

}
