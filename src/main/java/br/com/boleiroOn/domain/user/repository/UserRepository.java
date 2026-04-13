package br.com.boleiroOn.domain.user.repository;

import br.com.boleiroOn.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByLogin(String login);
    Optional<UserEntity> findByEmail(String email);
    boolean existsByLogin(String login);
    boolean existsByEmail(String email);
    List<UserEntity> findByStatus(boolean status);

    Optional<UserEntity> findByCreationToken(String token);
}
