package se.systementor.javasecstart.security;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface UserRepo extends CrudRepository<User, UUID> {
    @Query("SELECT u FROM User u WHERE u.username = :username")
    User getByUsername(@Param("username") String username);

    User findUserByResetToken(String resetToken);
    
    User findUserByUsername(String email);
}
