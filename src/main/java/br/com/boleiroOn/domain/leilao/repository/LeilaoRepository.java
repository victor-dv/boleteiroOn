package br.com.boleiroOn.domain.leilao.repository;

import br.com.boleiroOn.domain.leilao.entity.LeilaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeilaoRepository extends JpaRepository<LeilaoEntity, Long> {

    List<LeilaoEntity> findByStatus (boolean status);
}
