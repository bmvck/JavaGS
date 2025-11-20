package com.gs.gestaoativos.gateways.repositories;

import com.gs.gestaoativos.domains.Colaborador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ColaboradorRepository extends JpaRepository<Colaborador, Long> {
    Optional<Colaborador> findByCpfColab(String cpf);
    
    Optional<Colaborador> findByEmailColab(String email);
    
    List<Colaborador> findByStatusColab(String status);
    
    List<Colaborador> findByResponsavelIdColab(Long responsavelId);
    
    @Query(value = "SELECT * FROM colaborador WHERE id_colab = :id", nativeQuery = true)
    Optional<Colaborador> findByIdNative(@Param("id") Long id);
    
    @Query(value = "SELECT NVL(MAX(id_colab), 1000) FROM colaborador", nativeQuery = true)
    Long findMaxId();
    
    @Query("SELECT c FROM Colaborador c WHERE " +
           "(:nome IS NULL OR LOWER(c.nomeColab) LIKE LOWER(CONCAT('%', :nome, '%'))) AND " +
           "(:email IS NULL OR LOWER(c.emailColab) LIKE LOWER(CONCAT('%', :email, '%'))) AND " +
           "(:status IS NULL OR c.statusColab = :status) AND " +
           "(:area IS NULL OR LOWER(c.areaColab) LIKE LOWER(CONCAT('%', :area, '%')))")
    List<Colaborador> findByFilters(@Param("nome") String nome,
                                    @Param("email") String email,
                                    @Param("status") String status,
                                    @Param("area") String area);
}

