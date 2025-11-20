package com.gs.gestaoativos.gateways.repositories;

import com.gs.gestaoativos.domains.Ativo;
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
public interface AtivoRepository extends JpaRepository<Ativo, Integer> {
    
    @EntityGraph(attributePaths = {"categoria"})
    @Query("SELECT a FROM Ativo a")
    Page<Ativo> findAllWithCategoria(Pageable pageable);
    Optional<Ativo> findByNumeroSerie(String numeroSerie);
    
    List<Ativo> findByStatus(String status);
    
    List<Ativo> findByCategoriaIdCateg(Integer categoriaId);
    
    @EntityGraph(attributePaths = {"categoria"})
    @Query("SELECT a FROM Ativo a WHERE " +
           "(:marca IS NULL OR LOWER(a.marca) LIKE LOWER(CONCAT('%', :marca, '%'))) AND " +
           "(:modelo IS NULL OR LOWER(a.modelo) LIKE LOWER(CONCAT('%', :modelo, '%'))) AND " +
           "(:status IS NULL OR a.status = :status) AND " +
           "(:categoriaId IS NULL OR a.categoria.idCateg = :categoriaId)")
    List<Ativo> findByFilters(@Param("marca") String marca,
                              @Param("modelo") String modelo,
                              @Param("status") String status,
                              @Param("categoriaId") Integer categoriaId);
    
    @EntityGraph(attributePaths = {"categoria"})
    @Query("SELECT a FROM Ativo a WHERE a.idAtivo = :id")
    Optional<Ativo> findByIdNative(@Param("id") Integer id);
    
    @Query(value = "SELECT NVL(MAX(id_ativo), 0) FROM ativo", nativeQuery = true)
    Integer findMaxId();
}

