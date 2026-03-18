package br.com.boleiroOn.config.security;

import br.com.boleiroOn.domain.user.entity.UserEntity;
import br.com.boleiroOn.domain.user.enums.UserRole;
import br.com.boleiroOn.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminUserInitializr implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.default-password}")
    private String adminPassword;
    @Value("${app.admin.default-email}")
    private String adminEmail;
    @Value("${app.admin.default-login}")
    private String adminLogin;


    @Override
    public void run(String... args) throws Exception {
        if (!userRepository.existsByLogin("admin")) {
            UserEntity admin = new UserEntity();
            admin.setName("Administrador do Sistema");
            admin.setEmail(adminEmail);
            admin.setLogin(adminLogin);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setRole(UserRole.ADMIN);

            userRepository.save(admin);
        }
    }
}
