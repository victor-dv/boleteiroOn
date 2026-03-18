package br.com.boleiroOn.domain.user.service;

import br.com.boleiroOn.domain.user.dto.UserRegisterDto;
import br.com.boleiroOn.domain.user.entity.UserEntity;
import br.com.boleiroOn.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public UserEntity registerUser(UserRegisterDto userRegisterDto) {

        if (userRepository.existsByEmail(userRegisterDto.email())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Este e-mail já está cadastrado no sistema.");
        }
        if (userRepository.existsByLogin(userRegisterDto.login())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Este login já está em uso. Escolha outro.");
        }

        UserEntity user = new UserEntity();
        user.setName(userRegisterDto.name());
        user.setEmail(userRegisterDto.email());
        user.setLogin(userRegisterDto.login());
        user.setRole(userRegisterDto.role());

        user.setPassword(passwordEncoder.encode(userRegisterDto.password()));

        return userRepository.save(user);
    }


}
