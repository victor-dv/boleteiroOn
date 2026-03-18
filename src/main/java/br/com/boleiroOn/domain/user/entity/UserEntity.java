package br.com.boleiroOn.domain.user.entity;

import br.com.boleiroOn.domain.user.enums.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String login;
    private String password;
    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.BOLETEIRO;

}
