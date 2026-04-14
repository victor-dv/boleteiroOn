package br.com.boleiroOn.domain.user.controller;

import br.com.boleiroOn.domain.user.dto.*;
import br.com.boleiroOn.domain.user.entity.UserEntity;
import br.com.boleiroOn.domain.user.service.UserService;
import br.com.boleiroOn.shared.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @PatchMapping("/define-password")
    public ResponseEntity<ApiResponse<String>> defineInitialPassword(@RequestBody @Valid DefinePasswordDto dto) {
        userService.definiInitialPassword(dto);
        return ResponseEntity.ok(ApiResponse.success("Senha definida com sucesso", "Senha definida com sucesso"));
    }
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDto>> login(@RequestBody @Valid LoginRequestDto request) {
        LoginResponseDto response = userService.login(request);
        return ResponseEntity.ok(ApiResponse.success(response, "Login realizado com sucesso"));
    }
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDto>> getById(@PathVariable Long id) {
        var user = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.success(UserResponseDto.from(user), "Usuário obtido com sucesso"));
    }
    @GetMapping("/")
    public ResponseEntity<ApiResponse<Iterable<UserResponseDto>>> getAll() {
        List<UserResponseDto> users = userService.getAll().stream().map(UserResponseDto::from).toList();
        return ResponseEntity.ok(ApiResponse.success(users, "Usuários obtidos com sucesso"));
    }
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDto>> update(@PathVariable Long id, @RequestBody @Valid UserUpdateDto dto) {
        var user = userService.update(id, dto);
        return ResponseEntity.ok(ApiResponse.success(UserResponseDto.from(user), "Usuário atualizado com sucesso"));
    }
    @PatchMapping("/{id}/desativar")
    public ResponseEntity<ApiResponse<UserResponseDto>> deactivate(@PathVariable Long id) {
        var user = userService.falsoDelete(id);
        return ResponseEntity.ok(ApiResponse.success(UserResponseDto.from(user), "Usuário desativado com sucesso"));
    }
    @PatchMapping("/{id}/ativar")
    public ResponseEntity<ApiResponse<UserResponseDto>> ativar(@PathVariable Long id) {
        var user = userService.activate(id);
        return ResponseEntity.ok(ApiResponse.success(UserResponseDto.from(user), "Usuário ativado com sucesso"));
    }
    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<Iterable<UserResponseDto>>> getByStatus(@PathVariable boolean status) {
        List<UserResponseDto> users = userService.getByStatus(status).stream().map(UserResponseDto::from).toList();
        return ResponseEntity.ok(ApiResponse.success(users, "Usuários obtidos com sucesso"));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<UserEntity>> delete(@PathVariable Long id){
        var user = userService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(user, "Usuario deletado"));

    }

}
