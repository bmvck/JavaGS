package com.gs.gestaoativos.gateways.repositories;

import com.gs.gestaoativos.domains.Manutencao;
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
public interface ManutencaoRepository extends JpaRepository<Manutencao, Integer> {
    
    @EntityGraph(attributePaths = {"ativo"})
    @Query("SELECT m FROM Manutencao m")
    Page<Manutencao> findAllWithAtivo(Pageable pageable);
    List<Manutencao> findByTipoManutencao(String tipoManutencao);
    
    List<Manutencao> findByAtivoIdAtivo(Integer ativoId);
    
    @EntityGraph(attributePaths = {"ativo"})
    @Query("SELECT m FROM Manutencao m WHERE " +
           "(:tipoManutencao IS NULL OR m.tipoManutencao = :tipoManutencao) AND " +
           "(:ativoId IS NULL OR m.ativo.idAtivo = :ativoId) AND " +
           "(:dataInicio IS NULL OR m.dataInicio >= :dataInicio) AND " +
           "(:dataFim IS NULL OR m.dataFim <= :dataFim)")
    List<Manutencao> findByFilters(@Param("tipoManutencao") String tipoManutencao,
                                   @Param("ativoId") Integer ativoId,
                                   @Param("dataInicio") LocalDate dataInicio,
                                   @Param("dataFim") LocalDate dataFim);
    
    @EntityGraph(attributePaths = {"ativo"})
    @Query("SELECT m FROM Manutencao m WHERE m.idManutencao = :id")
    Optional<Manutencao> findByIdNative(@Param("id") Integer id);
    
    @Query(value = "SELECT NVL(MAX(id_manutencao), 7000) FROM manutencao", nativeQuery = true)
    Integer findMaxId();
}

