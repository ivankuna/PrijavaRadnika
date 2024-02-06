package hr.betaSoft.security.secRepo;

import hr.betaSoft.security.secModel.User;
import hr.betaSoft.security.userdto.UserDto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    User findByEmail(String email);

    User findByEmailToSend(String emailToSend);

    User findById(long id);
}
