package com.gs.gestaoativos.services;

import com.gs.gestaoativos.domains.Ativo;
import com.gs.gestaoativos.domains.DispositivoIoT;
import com.gs.gestaoativos.gateways.repositories.AtivoRepository;
import com.gs.gestaoativos.gateways.repositories.DispositivoIoTRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DispositivoIoTService {

    private final DispositivoIoTRepository dispositivoIoTRepository;
    private final AtivoRepository ativoRepository;

    @Transactional
    public DispositivoIoT criar(DispositivoIoT dispositivo, Integer ativoId) {
        Ativo ativo = ativoRepository.findByIdNative(ativoId)
                .orElseThrow(() -> new RuntimeException("Ativo não encontrado com ID: " + ativoId));
        
        if (dispositivoIoTRepository.findByIdentificadorHw(dispositivo.getIdentificadorHw()).isPresent()) {
            throw new RuntimeException("Dispositivo IoT com identificador " + dispositivo.getIdentificadorHw() + " já existe");
        }
        
        dispositivo.setAtivo(ativo);
        return dispositivoIoTRepository.save(dispositivo);
    }

    @Transactional
    public DispositivoIoT atualizar(Integer id, DispositivoIoT dispositivo, Integer ativoId) {
        DispositivoIoT existente = buscarPorId(id);
        
        if (ativoId != null) {
            Ativo ativo = ativoRepository.findByIdNative(ativoId)
                    .orElseThrow(() -> new RuntimeException("Ativo não encontrado com ID: " + ativoId));
            dispositivo.setAtivo(ativo);
        } else {
            dispositivo.setAtivo(existente.getAtivo());
        }
        
        dispositivo.setIdDisp(existente.getIdDisp());
        return dispositivoIoTRepository.save(dispositivo);
    }

    @Transactional(readOnly = true)
    public DispositivoIoT buscarPorId(Integer id) {
        return dispositivoIoTRepository.findByIdNative(id)
                .orElseThrow(() -> new RuntimeException("Dispositivo IoT não encontrado com ID: " + id));
    }

    @Transactional(readOnly = true)
    public Page<DispositivoIoT> listar(Pageable pageable) {
        return dispositivoIoTRepository.findAllWithAtivo(pageable);
    }

    @Transactional(readOnly = true)
    public List<DispositivoIoT> buscarPorFiltros(String identificadorHw, String status, Integer ativoId) {
        return dispositivoIoTRepository.findByFilters(identificadorHw, status, ativoId);
    }

    @Transactional
    public void deletar(Integer id) {
        if (!dispositivoIoTRepository.existsById(id)) {
            throw new RuntimeException("Dispositivo IoT não encontrado com ID: " + id);
        }
        dispositivoIoTRepository.deleteById(id);
    }
}

