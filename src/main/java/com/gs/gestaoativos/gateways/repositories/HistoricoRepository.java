package com.gs.gestaoativos.gateways.repositories;

import com.gs.gestaoativos.domains.Historico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface HistoricoRepository extends JpaRepository<Historico, Integer> {
    
    @EntityGraph(attributePaths = {"ativo", "colaborador"})
    @Query("SELECT h FROM Historico h")
    Page<Historico> findAllWithRelacionamentos(Pageable pageable);
    List<Historico> findByTipoMovimentacao(String tipoMovimentacao);
    
    List<Historico> findByAtivoIdAtivo(Integer ativoId);
    
    List<Historico> findByColaboradorIdColab(Long colaboradorId);
    
    @EntityGraph(attributePaths = {"ativo", "colaborador"})
    @Query("SELECT h FROM Historico h WHERE " +
           "(:tipoMovimentacao IS NULL OR h.tipoMovimentacao = :tipoMovimentacao) AND " +
           "(:ativoId IS NULL OR h.ativo.idAtivo = :ativoId) AND " +
           "(:colaboradorId IS NULL OR h.colaborador.idColab = :colaboradorId) AND " +
           "(:dataInicio IS NULL OR h.dataMovimentacao >= :dataInicio) AND " +
           "(:dataFim IS NULL OR h.dataMovimentacao <= :dataFim)")
    List<Historico> findByFilters(@Param("tipoMovimentacao") String tipoMovimentacao,
                                  @Param("ativoId") Integer ativoId,
                                  @Param("colaboradorId") Long colaboradorId,
                                  @Param("dataInicio") LocalDate dataInicio,
                                  @Param("dataFim") LocalDate dataFim);
    
    @EntityGraph(attributePaths = {"ativo", "colaborador"})
    @Query("SELECT h FROM Historico h WHERE h.idHistorico = :id")
    Optional<Historico> findByIdNative(@Param("id") Integer id);
    
    @Query(value = "SELECT NVL(MAX(id_historico), 6000) FROM historico", nativeQuery = true)
    Integer findMaxId();
}

