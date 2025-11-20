package com.gs.gestaoativos.gateways.repositories;

import com.gs.gestaoativos.domains.DispositivoIoT;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DispositivoIoTRepository extends JpaRepository<DispositivoIoT, Integer> {
    
    @EntityGraph(attributePaths = {"ativo"})
    @Query("SELECT d FROM DispositivoIoT d")
    Page<DispositivoIoT> findAllWithAtivo(Pageable pageable);
    Optional<DispositivoIoT> findByIdentificadorHw(String identificadorHw);
    
    List<DispositivoIoT> findByStatusDisp(String status);
    
    List<DispositivoIoT> findByAtivoIdAtivo(Integer ativoId);
    
    @EntityGraph(attributePaths = {"ativo"})
    @Query("SELECT d FROM DispositivoIoT d WHERE " +
           "(:identificadorHw IS NULL OR LOWER(d.identificadorHw) LIKE LOWER(CONCAT('%', :identificadorHw, '%'))) AND " +
           "(:status IS NULL OR d.statusDisp = :status) AND " +
           "(:ativoId IS NULL OR d.ativo.idAtivo = :ativoId)")
    List<DispositivoIoT> findByFilters(@Param("identificadorHw") String identificadorHw,
                                       @Param("status") String status,
                                       @Param("ativoId") Integer ativoId);
    
    @EntityGraph(attributePaths = {"ativo"})
    @Query("SELECT d FROM DispositivoIoT d WHERE d.idDisp = :id")
    Optional<DispositivoIoT> findByIdNative(@Param("id") Integer id);
}

