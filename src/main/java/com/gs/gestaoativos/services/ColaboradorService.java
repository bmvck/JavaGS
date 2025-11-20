package com.gs.gestaoativos.services;

import com.gs.gestaoativos.domains.Colaborador;
import com.gs.gestaoativos.gateways.repositories.ColaboradorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ColaboradorService {

    private final ColaboradorRepository colaboradorRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
    public Long buscarMaxId() {
        Long maxId = colaboradorRepository.findMaxId();
        // Teste: verificar quantos colaboradores existem
        long count = colaboradorRepository.count();
        System.out.println("DEBUG: Total de colaboradores no banco: " + count);
        System.out.println("DEBUG: Max ID retornado pela query: " + maxId);
        return maxId;
    }

    @Transactional
    public Colaborador criar(Colaborador colaborador, Long responsavelId) {
        try {
            // Gerar ID automaticamente se não fornecido
            if (colaborador.getIdColab() == null) {
                Long maxId = buscarMaxId();
                colaborador.setIdColab(maxId + 1);
                System.out.println("DEBUG: Max ID encontrado: " + maxId + ", Novo ID gerado: " + colaborador.getIdColab());
            }
            
            if (colaborador.getCpfColab() != null && colaboradorRepository.findByCpfColab(colaborador.getCpfColab()).isPresent()) {
                throw new RuntimeException("Colaborador com CPF " + colaborador.getCpfColab() + " já existe");
            }
            
            if (colaboradorRepository.findByEmailColab(colaborador.getEmailColab()).isPresent()) {
                throw new RuntimeException("Colaborador com email " + colaborador.getEmailColab() + " já existe");
            }
            
            if (responsavelId != null) {
                Colaborador responsavel = buscarPorId(responsavelId);
                colaborador.setResponsavel(responsavel);
            }
            
            // Criptografa a senha se fornecida e não vazia
            // Comportamento idêntico ao método definirSenha() para garantir consistência
            if (colaborador.getSenha() != null && !colaborador.getSenha().trim().isEmpty()) {
                colaborador.setSenha(passwordEncoder.encode(colaborador.getSenha()));
            }
            
            // Define role padrão se não fornecido
            if (colaborador.getRole() == null || colaborador.getRole().isEmpty()) {
                colaborador.setRole("USER");
            }
            
            Colaborador saved = colaboradorRepository.save(colaborador);
            colaboradorRepository.flush(); // Força o flush para o banco
            
            System.out.println("DEBUG: Colaborador salvo com ID: " + saved.getIdColab());
            System.out.println("DEBUG: Transação ativa: " + (colaboradorRepository instanceof org.springframework.data.jpa.repository.support.SimpleJpaRepository));
            
            return saved;
        } catch (Exception e) {
            System.err.println("ERRO ao criar colaborador: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Transactional
    public Colaborador atualizar(Long id, Colaborador colaborador, Long responsavelId) {
        Colaborador existente = buscarPorId(id);
        
        if (responsavelId != null && !responsavelId.equals(id)) {
            Colaborador responsavel = buscarPorId(responsavelId);
            colaborador.setResponsavel(responsavel);
        } else if (responsavelId == null) {
            colaborador.setResponsavel(null);
        }
        
        // Se senha foi fornecida e não está vazia, criptografa. Caso contrário, mantém a existente
        // Comportamento idêntico ao método definirSenha() para garantir consistência
        if (colaborador.getSenha() != null && !colaborador.getSenha().trim().isEmpty()) {
            colaborador.setSenha(passwordEncoder.encode(colaborador.getSenha()));
        } else {
            colaborador.setSenha(existente.getSenha());
        }
        
        // Mantém role existente se não fornecido
        if (colaborador.getRole() == null || colaborador.getRole().isEmpty()) {
            colaborador.setRole(existente.getRole() != null ? existente.getRole() : "USER");
        }
        
        colaborador.setIdColab(existente.getIdColab());
        return colaboradorRepository.save(colaborador);
    }

    @Transactional(readOnly = true)
    public Colaborador buscarPorId(Long id) {
        return colaboradorRepository.findByIdNative(id)
                .orElseThrow(() -> new RuntimeException("Colaborador não encontrado com ID: " + id));
    }

    @Transactional(readOnly = true)
    public Page<Colaborador> listar(Pageable pageable) {
        return colaboradorRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public List<Colaborador> buscarPorFiltros(String nome, String email, String status, String area) {
        return colaboradorRepository.findByFilters(nome, email, status, area);
    }

    @Transactional
    public void deletar(Long id) {
        if (!colaboradorRepository.existsById(id)) {
            throw new RuntimeException("Colaborador não encontrado com ID: " + id);
        }
        colaboradorRepository.deleteById(id);
    }

    @Transactional
    public void definirSenha(Long colaboradorId, String senha) {
        Colaborador colaborador = buscarPorId(colaboradorId);
        colaborador.setSenha(passwordEncoder.encode(senha));
        colaboradorRepository.save(colaborador);
    }
}

