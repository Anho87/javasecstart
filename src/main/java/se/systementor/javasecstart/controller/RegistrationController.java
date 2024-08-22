package se.systementor.javasecstart.controller;



import jakarta.mail.MessagingException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import se.systementor.javasecstart.security.User;
import se.systementor.javasecstart.security.UserRepo;
import se.systementor.javasecstart.utils.EmailService;
import se.systementor.javasecstart.utils.TokenGenerator;

import java.time.LocalDateTime;

@Controller
public class RegistrationController {

    
    private final UserRepo userRepository;
    private final EmailService emailService;

    public RegistrationController(UserRepo userRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
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
            Model model) throws MessagingException {

        if (userRepository.getByUsername(mail) != null) {
            model.addAttribute("feedback", "Email is already in use. Please choose another one.");
            return "register"; 
        }

        String verificationToken = TokenGenerator.generateToken(32);
        LocalDateTime expirationTime = LocalDateTime.now().plusHours(24); 
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hash = encoder.encode(password);
        User user = User.builder()
                .enabled(false) 
                .password(hash)
                .username(mail)
                .firstName(firstName)
                .emailVerificationToken(verificationToken)
                .emailTokenExpiration(expirationTime)
                .build();
        userRepository.save(user);
        emailService.sendVerificationEmail(mail,verificationToken);
        model.addAttribute("feedback", "Registration successful! Please check your email to verify your account.");
        return "login";
    }
}

