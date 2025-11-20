package com.gs.gestaoativos.services;

import com.gs.gestaoativos.domains.Ativo;
import com.gs.gestaoativos.domains.Categoria;
import com.gs.gestaoativos.gateways.repositories.AtivoRepository;
import com.gs.gestaoativos.gateways.repositories.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AtivoService {

    private final AtivoRepository ativoRepository;
    private final CategoriaRepository categoriaRepository;

    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
    public Integer buscarMaxId() {
        return ativoRepository.findMaxId();
    }

    @Transactional
    public Ativo criar(Ativo ativo, Integer categoriaId) {
        // Gerar ID automaticamente se não fornecido
        if (ativo.getIdAtivo() == null) {
            Integer maxId = buscarMaxId();
            ativo.setIdAtivo(maxId + 1);
            System.out.println("DEBUG: Max ID encontrado: " + maxId + ", Novo ID gerado: " + ativo.getIdAtivo());
        }
        
        Categoria categoria = categoriaRepository.findByIdNative(categoriaId)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada com ID: " + categoriaId));
        
        if (ativo.getNumeroSerie() != null && ativoRepository.findByNumeroSerie(ativo.getNumeroSerie()).isPresent()) {
            throw new RuntimeException("Ativo com número de série " + ativo.getNumeroSerie() + " já existe");
        }
        
        ativo.setCategoria(categoria);
        ativo.setDataUltAtualizacao(LocalDate.now());
        Ativo saved = ativoRepository.save(ativo);
        ativoRepository.flush(); // Força o flush para o banco
        return saved;
    }

    @Transactional
    public Ativo atualizar(Integer id, Ativo ativo, Integer categoriaId) {
        Ativo existente = buscarPorId(id);
        Categoria categoria = categoriaRepository.findByIdNative(categoriaId)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada com ID: " + categoriaId));
        
        ativo.setIdAtivo(existente.getIdAtivo());
        ativo.setCategoria(categoria);
        ativo.setDataUltAtualizacao(LocalDate.now());
        return ativoRepository.save(ativo);
    }

    @Transactional(readOnly = true)
    public Ativo buscarPorId(Integer id) {
        return ativoRepository.findByIdNative(id)
                .orElseThrow(() -> new RuntimeException("Ativo não encontrado com ID: " + id));
    }

    @Transactional(readOnly = true)
    public Page<Ativo> listar(Pageable pageable) {
        return ativoRepository.findAllWithCategoria(pageable);
    }

    @Transactional(readOnly = true)
    public List<Ativo> buscarPorFiltros(String marca, String modelo, String status, Integer categoriaId) {
        return ativoRepository.findByFilters(marca, modelo, status, categoriaId);
    }

    @Transactional
    public void deletar(Integer id) {
        if (!ativoRepository.existsById(id)) {
            throw new RuntimeException("Ativo não encontrado com ID: " + id);
        }
        ativoRepository.deleteById(id);
    }
}

