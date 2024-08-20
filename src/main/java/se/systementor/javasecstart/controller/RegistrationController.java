package se.systementor.javasecstart.controller;



import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import se.systementor.javasecstart.security.User;
import se.systementor.javasecstart.security.UserRepo;

@Controller
public class RegistrationController {

    
    private final UserRepo userRepository;

    public RegistrationController(UserRepo userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("feedback", null);
        return "register"; 
    }

    @PostMapping("/register")
    public String registerUser(
            @RequestParam("email") String mail,
            @RequestParam("firstname") String firstName,
            @RequestParam("password") String password,
            Model model) {

        if (userRepository.getByUsername(mail) != null) {
            model.addAttribute("feedback", "Email is already in use. Please choose another one.");
            return "register"; 
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hash = encoder.encode(password);
        User user = User.builder().enabled(true).password(hash).username(mail).firstName(firstName).build();
        userRepository.save(user);
        model.addAttribute("feedback", "Registration successful! Welcome, " + firstName + ".");
        return "login";
    }
}

