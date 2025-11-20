package com.gs.gestaoativos.services;

import com.gs.gestaoativos.domains.Ativo;
import com.gs.gestaoativos.domains.Manutencao;
import com.gs.gestaoativos.gateways.repositories.AtivoRepository;
import com.gs.gestaoativos.gateways.repositories.ManutencaoRepository;
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
public class ManutencaoService {

    private final ManutencaoRepository manutencaoRepository;
    private final AtivoRepository ativoRepository;

    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
    public Integer buscarMaxId() {
        return manutencaoRepository.findMaxId();
    }

    @Transactional
    public Manutencao criar(Manutencao manutencao, Integer ativoId) {
        // Gerar ID automaticamente se não fornecido
        if (manutencao.getIdManutencao() == null) {
            Integer maxId = buscarMaxId();
            manutencao.setIdManutencao(maxId + 1);
            System.out.println("DEBUG: Max ID encontrado: " + maxId + ", Novo ID gerado: " + manutencao.getIdManutencao());
        }
        
        Ativo ativo = ativoRepository.findByIdNative(ativoId)
                .orElseThrow(() -> new RuntimeException("Ativo não encontrado com ID: " + ativoId));
        
        manutencao.setAtivo(ativo);
        Manutencao saved = manutencaoRepository.save(manutencao);
        manutencaoRepository.flush(); // Força o flush para o banco
        return saved;
    }

    @Transactional
    public Manutencao atualizar(Integer id, Manutencao manutencao, Integer ativoId) {
        Manutencao existente = buscarPorId(id);
        
        if (ativoId != null) {
            Ativo ativo = ativoRepository.findByIdNative(ativoId)
                    .orElseThrow(() -> new RuntimeException("Ativo não encontrado com ID: " + ativoId));
            manutencao.setAtivo(ativo);
        } else {
            manutencao.setAtivo(existente.getAtivo());
        }
        
        manutencao.setIdManutencao(existente.getIdManutencao());
        return manutencaoRepository.save(manutencao);
    }

    @Transactional(readOnly = true)
    public Manutencao buscarPorId(Integer id) {
        return manutencaoRepository.findByIdNative(id)
                .orElseThrow(() -> new RuntimeException("Manutenção não encontrada com ID: " + id));
    }

    @Transactional(readOnly = true)
    public Page<Manutencao> listar(Pageable pageable) {
        return manutencaoRepository.findAllWithAtivo(pageable);
    }

    @Transactional(readOnly = true)
    public List<Manutencao> buscarPorFiltros(String tipoManutencao, Integer ativoId, 
                                             LocalDate dataInicio, LocalDate dataFim) {
        return manutencaoRepository.findByFilters(tipoManutencao, ativoId, dataInicio, dataFim);
    }

    @Transactional
    public void deletar(Integer id) {
        if (!manutencaoRepository.existsById(id)) {
            throw new RuntimeException("Manutenção não encontrada com ID: " + id);
        }
        manutencaoRepository.deleteById(id);
    }
}

