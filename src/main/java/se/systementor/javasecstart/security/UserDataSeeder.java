package se.systementor.javasecstart.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserDataSeeder {
    @Autowired
    UserRepo userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserDataSeeder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


    public void seedUsers() {
        if (userRepository.getByUsername("johan.johnsson@airbnb.se") == null) {
            addUser("johan.johnsson@airbnb.se", "Johan", "1");
        }
        if (userRepository.getByUsername("Andreas.holmberg@airbnb.se") == null) {
            addUser("Andreas.holmberg@airbnb.se", "Andreas", "2");
        }
        if (userRepository.getByUsername("Felix.Dahlberg@airbnb.se") == null) {
            addUser("Felix.Dahlberg@airbnb.se", "Felix", "3");
        }
    }

    private void addUser(String mail,String firstName, String password) {
        String hash = passwordEncoder.encode(password);
        User user = User.builder().enabled(true).password(hash).username(mail).firstName(firstName).build();
        userRepository.save(user);
    }

}
