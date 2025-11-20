package com.gs.gestaoativos.gateways.controllers;

import com.gs.gestaoativos.domains.Categoria;
import com.gs.gestaoativos.gateways.dtos.CategoriaRequestDto;
import com.gs.gestaoativos.gateways.dtos.CategoriaResponseDto;
import com.gs.gestaoativos.services.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
@Tag(name = "Categorias", description = "Endpoints para gerenciamento de categorias de ativos")
public class CategoriaController {

    private final CategoriaService categoriaService;

    @PostMapping
    @Operation(summary = "Criar categoria", description = "Cria uma nova categoria de ativo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Categoria criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @ResponseStatus(HttpStatus.CREATED)
    public CategoriaResponseDto criar(@Valid @RequestBody CategoriaRequestDto dto) {
        Categoria categoria = categoriaService.criar(dto.toEntity());
        return CategoriaResponseDto.fromEntity(categoria);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar categoria", description = "Atualiza uma categoria existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoria atualizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    })
    public CategoriaResponseDto atualizar(@PathVariable Integer id, @Valid @RequestBody CategoriaRequestDto dto) {
        Categoria categoria = categoriaService.atualizar(id, dto.toEntity());
        return CategoriaResponseDto.fromEntity(categoria);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar categoria por ID", description = "Retorna uma categoria específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoria encontrada"),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    })
    public CategoriaResponseDto buscarPorId(@PathVariable Integer id) {
        Categoria categoria = categoriaService.buscarPorId(id);
        return CategoriaResponseDto.fromEntity(categoria);
    }

    @GetMapping
    @Operation(summary = "Listar categorias", description = "Retorna uma lista paginada de categorias. Campos válidos para ordenação: nomeCateg, descCateg")
    public ResponseEntity<Page<CategoriaResponseDto>> listar(
            @ParameterObject @PageableDefault(size = 10, sort = "nomeCateg", direction = Sort.Direction.ASC) Pageable pageable,
            @Parameter(description = "Filtrar por nome") @RequestParam(required = false) String nome) {
        
        if (nome != null && !nome.isEmpty()) {
            List<Categoria> categorias = categoriaService.buscarPorNome(nome);
            List<CategoriaResponseDto> dtos = categorias.stream()
                    .map(CategoriaResponseDto::fromEntity)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(new org.springframework.data.domain.PageImpl<>(dtos, pageable, dtos.size()));
        }
        
        Page<Categoria> categorias = categoriaService.listar(pageable);
        Page<CategoriaResponseDto> dtos = categorias.map(CategoriaResponseDto::fromEntity);
        return ResponseEntity.ok(dtos);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar categoria", description = "Remove uma categoria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Categoria deletada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Integer id) {
        categoriaService.deletar(id);
    }
}

