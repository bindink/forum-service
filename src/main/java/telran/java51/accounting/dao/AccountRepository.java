package telran.java51.accounting.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import telran.java51.accounting.model.User;

import java.util.Optional;

@Repository
public interface AccountRepository extends CrudRepository<User, String> {
    Optional<User> findByLogin (String login);
    void deleteByLogin (String login);
}
