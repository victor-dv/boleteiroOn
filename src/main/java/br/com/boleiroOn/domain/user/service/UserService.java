package br.com.boleiroOn.domain.user.service;

import br.com.boleiroOn.config.infra.configs.JwtToken;
import br.com.boleiroOn.config.infra.email.service.EnviarDefinicaoSenhaService;
import br.com.boleiroOn.domain.user.dto.*;
import br.com.boleiroOn.domain.user.entity.UserEntity;
import br.com.boleiroOn.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EnviarDefinicaoSenhaService enviarDefinicaoSenhaService;
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

        user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));

        String token =  UUID.randomUUID().toString();
        user.setCreationToken(token);
        user.setTokenExpiration(LocalDateTime.now().plusHours(10));

        enviarDefinicaoSenhaService.enviarEmailConvite(user.getLogin(), user.getEmail(), token);

        return userRepository.save(user);
    }
    @Transactional
    public void definiInitialPassword(DefinePasswordDto dto) {
        UserEntity user = userRepository.findByCreationToken(dto.token())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token inválido ou não encontrado."));

        if (user.getTokenExpiration().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Este link de convite expirou. Solicite um novo reenvio.");
        }

        user.setPassword(passwordEncoder.encode(dto.newPassword()));

        user.setCreationToken(null);
        user.setTokenExpiration(null);

        userRepository.save(user);
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

    public UserEntity getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
    }
    public List<UserEntity> getAll(){
        return userRepository.findAll();
    }

    public UserEntity update(Long id, UserUpdateDto data){
        var user = userRepository.findById(id)
                .orElseThrow(() -> new  ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        user.setName(data.name() != null ? data.name() : user.getName());
        if (data.login() != null && !data.login().isBlank() && !data.login().equals(user.getLogin())) {
            if (userRepository.existsByLogin(data.login())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Este login já está em uso. Escolha outro.");
            }
            user.setLogin(data.login());
        }
        user.setRole(data.role() != null ? data.role() : user.getRole());

        if(data.password() != null && !data.password().isBlank()){

            if (data.oldPassword() == null || data.oldPassword().isBlank()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A senha antiga é obrigatória para alterar a senha.");
            }
            if (!passwordEncoder.matches(data.oldPassword(), user.getPassword())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A senha antiga está incorreta.");
            }
            user.setPassword(passwordEncoder.encode(data.password()));
        }
        return userRepository.save(user);
    }

    public UserEntity falsoDelete(Long id){
        var user = userRepository.findById(id)
                .orElseThrow(() -> new  ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
        user.setStatus(false);
        return userRepository.save(user);
    }
    public UserEntity activate(Long id){
        var user = userRepository.findById(id)
                .orElseThrow(() -> new  ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
        user.setStatus(true);
        return userRepository.save(user);
    }

    public List<UserEntity> getByStatus(boolean status){
        return userRepository.findByStatus(status);
    }

    public UserEntity delete(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new  ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
        userRepository.delete(user);
        return user;
    }


}
