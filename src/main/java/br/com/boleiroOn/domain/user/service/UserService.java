package br.com.boleiroOn.domain.user.service;

import br.com.boleiroOn.config.infra.configs.JwtToken;
import br.com.boleiroOn.domain.user.dto.LoginRequestDto;
import br.com.boleiroOn.domain.user.dto.LoginResponseDto;
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
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtToken jwtToken;

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

    public LoginResponseDto login(LoginRequestDto request) {
        var user = userRepository.findByLogin(request.login())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais inválidas"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais inválidas");
        }

        String token = jwtToken.generateToken(user.getLogin(), user.getId(), user.getRole().name());
        return new LoginResponseDto(token, user.getName(), user.getEmail());
    }


}
