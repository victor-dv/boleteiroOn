package br.com.boleiroOn.domain.arrematacao.repository;

import br.com.boleiroOn.domain.arrematacao.entity.ArrematacaoEntity;
import br.com.boleiroOn.domain.arrematacao.enums.StatusArrematacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArrematacaoRepository extends JpaRepository<ArrematacaoEntity, Long> {
    boolean existsByLoteIdAndStatusNot(Long loteId, StatusArrematacao status);
}
