package com.gs.gestaoativos.gateways.repositories;

import com.gs.gestaoativos.domains.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
    Optional<Categoria> findByNomeCateg(String nomeCateg);
    
    @Query("SELECT c FROM Categoria c WHERE LOWER(c.nomeCateg) LIKE LOWER(CONCAT('%', :nome, '%'))")
    List<Categoria> findByNomeCategContainingIgnoreCase(@Param("nome") String nome);
    
    @Query(value = "SELECT * FROM categoria WHERE id_categ = :id", nativeQuery = true)
    Optional<Categoria> findByIdNative(@Param("id") Integer id);
    
    @Query(value = "SELECT NVL(MAX(id_categ), 0) FROM categoria", nativeQuery = true)
    Integer findMaxId();
}

