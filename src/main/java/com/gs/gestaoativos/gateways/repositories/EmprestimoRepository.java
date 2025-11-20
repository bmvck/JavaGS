package com.gs.gestaoativos.gateways.repositories;

import com.gs.gestaoativos.domains.Emprestimo;
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
public interface EmprestimoRepository extends JpaRepository<Emprestimo, Integer> {
    
    @EntityGraph(attributePaths = {"ativo", "colaborador"})
    @Query("SELECT e FROM Emprestimo e")
    Page<Emprestimo> findAllWithRelacionamentos(Pageable pageable);
    List<Emprestimo> findByStatusEmprestimo(String status);
    
    List<Emprestimo> findByAtivoIdAtivo(Integer ativoId);
    
    List<Emprestimo> findByColaboradorIdColab(Long colaboradorId);
    
    @EntityGraph(attributePaths = {"ativo", "colaborador"})
    @Query("SELECT e FROM Emprestimo e WHERE " +
           "(:status IS NULL OR e.statusEmprestimo = :status) AND " +
           "(:ativoId IS NULL OR e.ativo.idAtivo = :ativoId) AND " +
           "(:colaboradorId IS NULL OR e.colaborador.idColab = :colaboradorId) AND " +
           "(:dataInicio IS NULL OR e.dataEmprestimo >= :dataInicio) AND " +
           "(:dataFim IS NULL OR e.dataEmprestimo <= :dataFim)")
    List<Emprestimo> findByFilters(@Param("status") String status,
                                   @Param("ativoId") Integer ativoId,
                                   @Param("colaboradorId") Long colaboradorId,
                                   @Param("dataInicio") LocalDate dataInicio,
                                   @Param("dataFim") LocalDate dataFim);
    
    @EntityGraph(attributePaths = {"ativo", "colaborador"})
    @Query("SELECT e FROM Emprestimo e WHERE e.idEmprestimo = :id")
    Optional<Emprestimo> findByIdNative(@Param("id") Integer id);
    
    @Query(value = "SELECT NVL(MAX(id_emprestimo), 5000) FROM emprestimo", nativeQuery = true)
    Integer findMaxId();
}

