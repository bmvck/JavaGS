package com.gs.gestaoativos.services;

import com.gs.gestaoativos.domains.Categoria;
import com.gs.gestaoativos.gateways.repositories.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
    public Integer buscarMaxId() {
        return categoriaRepository.findMaxId();
    }

    @Transactional
    public Categoria criar(Categoria categoria) {
        // Gerar ID automaticamente se não fornecido
        if (categoria.getIdCateg() == null) {
            Integer maxId = buscarMaxId();
            categoria.setIdCateg(maxId + 10);
            System.out.println("DEBUG: Max ID encontrado: " + maxId + ", Novo ID gerado: " + categoria.getIdCateg());
        } else if (categoriaRepository.existsById(categoria.getIdCateg())) {
            throw new RuntimeException("Categoria com ID " + categoria.getIdCateg() + " já existe");
        }
        Categoria saved = categoriaRepository.save(categoria);
        categoriaRepository.flush(); // Força o flush para o banco
        return saved;
    }

    @Transactional
    public Categoria atualizar(Integer id, Categoria categoria) {
        Categoria existente = buscarPorId(id);
        categoria.setIdCateg(existente.getIdCateg());
        return categoriaRepository.save(categoria);
    }

    @Transactional(readOnly = true)
    public Categoria buscarPorId(Integer id) {
        return categoriaRepository.findByIdNative(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada com ID: " + id));
    }

    @Transactional(readOnly = true)
    public Page<Categoria> listar(Pageable pageable) {
        return categoriaRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public List<Categoria> buscarPorNome(String nome) {
        return categoriaRepository.findByNomeCategContainingIgnoreCase(nome);
    }

    @Transactional
    public void deletar(Integer id) {
        if (!categoriaRepository.existsById(id)) {
            throw new RuntimeException("Categoria não encontrada com ID: " + id);
        }
        categoriaRepository.deleteById(id);
    }
}

