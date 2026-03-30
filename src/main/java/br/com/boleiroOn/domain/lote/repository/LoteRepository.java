package br.com.boleiroOn.domain.lote.repository;

import br.com.boleiroOn.domain.lote.entity.LoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LoteRepository extends JpaRepository<LoteEntity, Long> {
    boolean existsByLeilaoIdAndNumeroLote(Long leilaoId, Integer numeroLote);

    Optional<LoteEntity> findByLeilaoIdAndNumeroLote(Long leilaoId, Integer numeroLote);
    List<LoteEntity> findByLeilaoId(Long leilaoId);
}
