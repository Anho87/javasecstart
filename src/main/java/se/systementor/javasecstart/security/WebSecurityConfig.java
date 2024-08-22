package se.systementor.javasecstart.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    // Replace BCryptPasswordEncoder with a custom MD5-based PasswordEncoder
    @Bean
    public PasswordEncoder md5PasswordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return hashWithMD5(rawPassword.toString());
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return encodedPassword.equals(hashWithMD5(rawPassword.toString()));
            }

            private String hashWithMD5(String input) {
                try {
                    MessageDigest md = MessageDigest.getInstance("MD5");
                    byte[] hashBytes = md.digest(input.getBytes());
                    StringBuilder hexString = new StringBuilder();
                    for (byte b : hashBytes) {
                        hexString.append(String.format("%02x", b));
                    }
                    return hexString.toString();
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException("MD5 algorithm not found", e);
                }
            }
        };
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(md5PasswordEncoder()); // Set MD5-based PasswordEncoder

        return authProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/login", "/resetPasswordLink/**", "/handleResetPassword/**", "/resetPassword/**", "/updatedPassword/**", "/register", "/verify").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .successForwardUrl("/")
                        .permitAll()
                )
                .logout((logout) -> {
                    logout.permitAll();
                    logout.logoutSuccessUrl("/login");
                })
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }
}
