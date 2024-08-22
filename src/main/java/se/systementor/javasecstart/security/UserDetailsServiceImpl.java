package se.systementor.javasecstart.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder; // Use PasswordEncoder

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.getByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("Could not find user: " + username);
        }
        return new ConcreteUserDetails(user);
    }

    public List<UserDetails> getAllUsers() {
        Iterable<User> users = userRepo.findAll();

        return StreamSupport.stream(users.spliterator(), false)
                .map(ConcreteUserDetails::new)
                .collect(Collectors.toList());
    }

    public String addUser(String mail, String password) {
        try {
            loadUserByUsername(mail);
            return "User already exists";
        } catch (UsernameNotFoundException e) {
            String hash = passwordEncoder.encode(password); // Use PasswordEncoder to hash the password
            User user = User.builder().enabled(true).password(hash).username(mail).build();
            userRepo.save(user);
            return "New user added";
        }
    }

    public String updateUser(String oldMail, String newMail, String password) {
        try {
            User user = userRepo.getByUsername(oldMail);
            if (user == null) {
                return "User not found";
            }
            if (!newMail.equals(oldMail)) {
                user.setUsername(newMail);
            }
            if (password != null && !password.isEmpty()) {
                String hash = passwordEncoder.encode(password); // Use PasswordEncoder to hash the password
                user.setPassword(hash);
            }
            userRepo.save(user);
            return "User updated successfully";
        } catch (UsernameNotFoundException e) {
            return "User not found";
        }
    }

    public String deleteUser(String username) {
        User user = userRepo.getByUsername(username);
        if (user == null) {
            return "User not found";
        }
        userRepo.delete(user);
        return "User deleted successfully";
    }

    public User getUserByResetToken(String token) {
        return userRepo.findUserByResetToken(token);
    }

    public User getUserByEmail(String email) {
        return userRepo.getByUsername(email);
    }

    public void saveUserToken(User user) {
        userRepo.save(user);
    }

    public String updatePassword(String token, String newPassword) {

        User user = userRepo.findUserByResetToken(token);
        LocalDateTime now = LocalDateTime.now();
        if (user == null) {
            return "Your password wasn't updated";

        } else if (now.isAfter(user.getResetTokenCreationTime())) {
            resetUserTokens(user);
            return "Time has expired for you password reset link";
        } else {
            String hashedPassword = passwordEncoder.encode(newPassword); // Use PasswordEncoder to hash the password
            user.setPassword(hashedPassword);
            resetUserTokens(user);
            userRepo.save(user);
            return "Password updated";
        }
    }

    private void resetUserTokens(User user) {
        user.setResetToken(null);
        user.setResetTokenCreationTime(null);
    }
}
