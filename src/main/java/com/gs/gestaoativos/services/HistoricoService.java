package com.gs.gestaoativos.services;

import com.gs.gestaoativos.domains.Ativo;
import com.gs.gestaoativos.domains.Colaborador;
import com.gs.gestaoativos.domains.Historico;
import com.gs.gestaoativos.gateways.repositories.AtivoRepository;
import com.gs.gestaoativos.gateways.repositories.ColaboradorRepository;
import com.gs.gestaoativos.gateways.repositories.HistoricoRepository;
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
public class HistoricoService {

    private final HistoricoRepository historicoRepository;
    private final AtivoRepository ativoRepository;
    private final ColaboradorRepository colaboradorRepository;

    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
    public Integer buscarMaxId() {
        return historicoRepository.findMaxId();
    }

    @Transactional
    public Historico criar(Historico historico, Integer ativoId, Long colaboradorId) {
        // Gerar ID automaticamente se não fornecido
        if (historico.getIdHistorico() == null) {
            Integer maxId = buscarMaxId();
            historico.setIdHistorico(maxId + 1);
            System.out.println("DEBUG: Max ID encontrado: " + maxId + ", Novo ID gerado: " + historico.getIdHistorico());
        }
        
        Ativo ativo = ativoRepository.findByIdNative(ativoId)
                .orElseThrow(() -> new RuntimeException("Ativo não encontrado com ID: " + ativoId));
        
        Colaborador colaborador = colaboradorRepository.findByIdNative(colaboradorId)
                .orElseThrow(() -> new RuntimeException("Colaborador não encontrado com ID: " + colaboradorId));
        
        historico.setAtivo(ativo);
        historico.setColaborador(colaborador);
        
        if (historico.getDataMovimentacao() == null) {
            historico.setDataMovimentacao(LocalDate.now());
        }
        
        Historico saved = historicoRepository.save(historico);
        historicoRepository.flush(); // Força o flush para o banco
        return saved;
    }

    @Transactional
    public Historico atualizar(Integer id, Historico historico, Integer ativoId, Long colaboradorId) {
        Historico existente = buscarPorId(id);
        
        if (ativoId != null) {
            Ativo ativo = ativoRepository.findByIdNative(ativoId)
                    .orElseThrow(() -> new RuntimeException("Ativo não encontrado com ID: " + ativoId));
            historico.setAtivo(ativo);
        } else {
            historico.setAtivo(existente.getAtivo());
        }
        
        if (colaboradorId != null) {
            Colaborador colaborador = colaboradorRepository.findByIdNative(colaboradorId)
                    .orElseThrow(() -> new RuntimeException("Colaborador não encontrado com ID: " + colaboradorId));
            historico.setColaborador(colaborador);
        } else {
            historico.setColaborador(existente.getColaborador());
        }
        
        historico.setIdHistorico(existente.getIdHistorico());
        return historicoRepository.save(historico);
    }

    @Transactional(readOnly = true)
    public Historico buscarPorId(Integer id) {
        return historicoRepository.findByIdNative(id)
                .orElseThrow(() -> new RuntimeException("Histórico não encontrado com ID: " + id));
    }

    @Transactional(readOnly = true)
    public Page<Historico> listar(Pageable pageable) {
        return historicoRepository.findAllWithRelacionamentos(pageable);
    }

    @Transactional(readOnly = true)
    public List<Historico> buscarPorFiltros(String tipoMovimentacao, Integer ativoId, Long colaboradorId,
                                            LocalDate dataInicio, LocalDate dataFim) {
        return historicoRepository.findByFilters(tipoMovimentacao, ativoId, colaboradorId, dataInicio, dataFim);
    }

    @Transactional
    public void deletar(Integer id) {
        if (!historicoRepository.existsById(id)) {
            throw new RuntimeException("Histórico não encontrado com ID: " + id);
        }
        historicoRepository.deleteById(id);
    }
}

