package com.gs.gestaoativos.services;

import com.gs.gestaoativos.domains.Ativo;
import com.gs.gestaoativos.domains.Colaborador;
import com.gs.gestaoativos.domains.Emprestimo;
import com.gs.gestaoativos.gateways.repositories.AtivoRepository;
import com.gs.gestaoativos.gateways.repositories.ColaboradorRepository;
import com.gs.gestaoativos.gateways.repositories.EmprestimoRepository;
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
public class EmprestimoService {

    private final EmprestimoRepository emprestimoRepository;
    private final AtivoRepository ativoRepository;
    private final ColaboradorRepository colaboradorRepository;

    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
    public Integer buscarMaxId() {
        return emprestimoRepository.findMaxId();
    }

    @Transactional
    public Emprestimo criar(Emprestimo emprestimo, Integer ativoId, Long colaboradorId) {
        // Gerar ID automaticamente se não fornecido
        if (emprestimo.getIdEmprestimo() == null) {
            Integer maxId = buscarMaxId();
            emprestimo.setIdEmprestimo(maxId + 1);
            System.out.println("DEBUG: Max ID encontrado: " + maxId + ", Novo ID gerado: " + emprestimo.getIdEmprestimo());
        }
        
        Ativo ativo = ativoRepository.findByIdNative(ativoId)
                .orElseThrow(() -> new RuntimeException("Ativo não encontrado com ID: " + ativoId));
        
        Colaborador colaborador = colaboradorRepository.findByIdNative(colaboradorId)
                .orElseThrow(() -> new RuntimeException("Colaborador não encontrado com ID: " + colaboradorId));
        
        emprestimo.setAtivo(ativo);
        emprestimo.setColaborador(colaborador);
        
        if (emprestimo.getDataEmprestimo() == null) {
            emprestimo.setDataEmprestimo(LocalDate.now());
        }
        
        Emprestimo saved = emprestimoRepository.save(emprestimo);
        emprestimoRepository.flush(); // Força o flush para o banco
        return saved;
    }

    @Transactional
    public Emprestimo atualizar(Integer id, Emprestimo emprestimo, Integer ativoId, Long colaboradorId) {
        Emprestimo existente = buscarPorId(id);
        
        if (ativoId != null) {
            Ativo ativo = ativoRepository.findByIdNative(ativoId)
                    .orElseThrow(() -> new RuntimeException("Ativo não encontrado com ID: " + ativoId));
            emprestimo.setAtivo(ativo);
        } else {
            emprestimo.setAtivo(existente.getAtivo());
        }
        
        if (colaboradorId != null) {
            Colaborador colaborador = colaboradorRepository.findByIdNative(colaboradorId)
                    .orElseThrow(() -> new RuntimeException("Colaborador não encontrado com ID: " + colaboradorId));
            emprestimo.setColaborador(colaborador);
        } else {
            emprestimo.setColaborador(existente.getColaborador());
        }
        
        emprestimo.setIdEmprestimo(existente.getIdEmprestimo());
        return emprestimoRepository.save(emprestimo);
    }

    @Transactional(readOnly = true)
    public Emprestimo buscarPorId(Integer id) {
        return emprestimoRepository.findByIdNative(id)
                .orElseThrow(() -> new RuntimeException("Empréstimo não encontrado com ID: " + id));
    }

    @Transactional(readOnly = true)
    public Page<Emprestimo> listar(Pageable pageable) {
        return emprestimoRepository.findAllWithRelacionamentos(pageable);
    }

    @Transactional(readOnly = true)
    public List<Emprestimo> buscarPorFiltros(String status, Integer ativoId, Long colaboradorId, 
                                             LocalDate dataInicio, LocalDate dataFim) {
        return emprestimoRepository.findByFilters(status, ativoId, colaboradorId, dataInicio, dataFim);
    }

    @Transactional
    public void deletar(Integer id) {
        if (!emprestimoRepository.existsById(id)) {
            throw new RuntimeException("Empréstimo não encontrado com ID: " + id);
        }
        emprestimoRepository.deleteById(id);
    }
}

