package br.com.boleiroOn.config.infra.email.repository;

import br.com.boleiroOn.config.infra.email.entity.VerificationEmailToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationEmailTokenRepository extends JpaRepository<VerificationEmailToken, Long> {
    Optional<VerificationEmailToken> findByToken(String token);}
