package com.gs.gestaoativos.gateways.controllers;

import com.gs.gestaoativos.domains.Ativo;
import com.gs.gestaoativos.gateways.dtos.AtivoRequestDto;
import com.gs.gestaoativos.gateways.dtos.AtivoResponseDto;
import com.gs.gestaoativos.services.AtivoService;
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
@RequestMapping("/api/ativos")
@RequiredArgsConstructor
@Tag(name = "Ativos", description = "Endpoints para gerenciamento de ativos")
public class AtivoController {

    private final AtivoService ativoService;

    @PostMapping
    @Operation(summary = "Criar ativo", description = "Cria um novo ativo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Ativo criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @ResponseStatus(HttpStatus.CREATED)
    public AtivoResponseDto criar(@Valid @RequestBody AtivoRequestDto dto) {
        Ativo ativo = ativoService.criar(dto.toEntity(), dto.getCategoriaIdCateg());
        return AtivoResponseDto.fromEntity(ativo);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar ativo", description = "Atualiza um ativo existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ativo atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Ativo não encontrado")
    })
    public AtivoResponseDto atualizar(@PathVariable Integer id, @Valid @RequestBody AtivoRequestDto dto) {
        Ativo ativo = ativoService.atualizar(id, dto.toEntity(), dto.getCategoriaIdCateg());
        return AtivoResponseDto.fromEntity(ativo);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar ativo por ID", description = "Retorna um ativo específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ativo encontrado"),
            @ApiResponse(responseCode = "404", description = "Ativo não encontrado")
    })
    public AtivoResponseDto buscarPorId(@PathVariable Integer id) {
        Ativo ativo = ativoService.buscarPorId(id);
        return AtivoResponseDto.fromEntity(ativo);
    }

    @GetMapping
    @Operation(summary = "Listar ativos", description = "Retorna uma lista paginada de ativos com filtros opcionais. Campos válidos para ordenação: marca, modelo, numeroSerie, status, dataAquisicao")
    public ResponseEntity<Page<AtivoResponseDto>> listar(
            @ParameterObject @PageableDefault(size = 10, sort = "marca", direction = Sort.Direction.ASC) Pageable pageable,
            @Parameter(description = "Filtrar por marca") @RequestParam(required = false) String marca,
            @Parameter(description = "Filtrar por modelo") @RequestParam(required = false) String modelo,
            @Parameter(description = "Filtrar por status") @RequestParam(required = false) String status,
            @Parameter(description = "Filtrar por ID da categoria") @RequestParam(required = false) Integer categoriaId) {
        
        if (marca != null || modelo != null || status != null || categoriaId != null) {
            List<Ativo> ativos = ativoService.buscarPorFiltros(marca, modelo, status, categoriaId);
            List<AtivoResponseDto> dtos = ativos.stream()
                    .map(AtivoResponseDto::fromEntity)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(new org.springframework.data.domain.PageImpl<>(dtos, pageable, dtos.size()));
        }
        
        Page<Ativo> ativos = ativoService.listar(pageable);
        Page<AtivoResponseDto> dtos = ativos.map(AtivoResponseDto::fromEntity);
        return ResponseEntity.ok(dtos);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar ativo", description = "Remove um ativo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Ativo deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Ativo não encontrado")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Integer id) {
        ativoService.deletar(id);
    }
}

