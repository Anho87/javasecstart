package se.systementor.javasecstart.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import se.systementor.javasecstart.security.ConcreteUserDetails;
import se.systementor.javasecstart.security.UserDetailsServiceImpl;

import java.security.Principal;

@Controller
public class HomeController {
    

    @RequestMapping("/")
    String empty(Model model, @AuthenticationPrincipal ConcreteUserDetails userDetails)
    {
        if (userDetails != null) {
            String firstName = userDetails.getFirstName(); 
            model.addAttribute("firstName", firstName);
        }
        model.addAttribute("activeFunction", "home");
        
//        setupVersion(model);

//        model.addAttribute("dogs", dogRepository.findAll());
        return "home";
    }}
