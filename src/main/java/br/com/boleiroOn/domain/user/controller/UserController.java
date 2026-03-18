package br.com.boleiroOn.domain.user.controller;

import br.com.boleiroOn.domain.user.dto.LoginRequestDto;
import br.com.boleiroOn.domain.user.dto.LoginResponseDto;
import br.com.boleiroOn.domain.user.dto.UserRegisterDto;
import br.com.boleiroOn.domain.user.dto.UserResponseDto;
import br.com.boleiroOn.domain.user.service.UserService;
import br.com.boleiroOn.shared.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponseDto>> register(@RequestBody @Valid UserRegisterDto dto) {
            var user = userService.registerUser(dto);
            return ResponseEntity.ok(ApiResponse.success(UserResponseDto.from(user), "Usuário registrado com sucesso"));
    }
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDto>> login(@RequestBody @Valid LoginRequestDto request) {
        LoginResponseDto response = userService.login(request);
        return ResponseEntity.ok(ApiResponse.success(response, "Login realizado com sucesso"));
    }



}
