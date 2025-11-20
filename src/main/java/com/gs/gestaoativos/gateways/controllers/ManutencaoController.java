package com.gs.gestaoativos.gateways.controllers;

import com.gs.gestaoativos.domains.Manutencao;
import com.gs.gestaoativos.gateways.dtos.ManutencaoRequestDto;
import com.gs.gestaoativos.gateways.dtos.ManutencaoResponseDto;
import com.gs.gestaoativos.services.ManutencaoService;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/manutencoes")
@RequiredArgsConstructor
@Tag(name = "Manutenções", description = "Endpoints para gerenciamento de manutenções de ativos")
public class ManutencaoController {

    private final ManutencaoService manutencaoService;

    @PostMapping
    @Operation(summary = "Criar manutenção", description = "Cria um novo registro de manutenção")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Manutenção criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @ResponseStatus(HttpStatus.CREATED)
    public ManutencaoResponseDto criar(@Valid @RequestBody ManutencaoRequestDto dto) {
        Manutencao manutencao = manutencaoService.criar(dto.toEntity(), dto.getAtivoIdAtivo());
        return ManutencaoResponseDto.fromEntity(manutencao);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar manutenção", description = "Atualiza um registro de manutenção existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Manutenção atualizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Manutenção não encontrada")
    })
    public ManutencaoResponseDto atualizar(@PathVariable Integer id, @Valid @RequestBody ManutencaoRequestDto dto) {
        Manutencao manutencao = manutencaoService.atualizar(id, dto.toEntity(), dto.getAtivoIdAtivo());
        return ManutencaoResponseDto.fromEntity(manutencao);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar manutenção por ID", description = "Retorna um registro de manutenção específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Manutenção encontrada"),
            @ApiResponse(responseCode = "404", description = "Manutenção não encontrada")
    })
    public ManutencaoResponseDto buscarPorId(@PathVariable Integer id) {
        Manutencao manutencao = manutencaoService.buscarPorId(id);
        return ManutencaoResponseDto.fromEntity(manutencao);
    }

    @GetMapping
    @Operation(summary = "Listar manutenções", description = "Retorna uma lista paginada de manutenções com filtros opcionais. Campos válidos para ordenação: dataInicio, dataFim, tipoManutencao, custo")
    public ResponseEntity<Page<ManutencaoResponseDto>> listar(
            @ParameterObject @PageableDefault(size = 10, sort = "dataInicio", direction = Sort.Direction.DESC) Pageable pageable,
            @Parameter(description = "Filtrar por tipo de manutenção") @RequestParam(required = false) String tipoManutencao,
            @Parameter(description = "Filtrar por ID do ativo") @RequestParam(required = false) Integer ativoId,
            @Parameter(description = "Filtrar por data de início") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @Parameter(description = "Filtrar por data de fim") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        
        if (tipoManutencao != null || ativoId != null || dataInicio != null || dataFim != null) {
            List<Manutencao> manutencoes = manutencaoService.buscarPorFiltros(tipoManutencao, ativoId, dataInicio, dataFim);
            List<ManutencaoResponseDto> dtos = manutencoes.stream()
                    .map(ManutencaoResponseDto::fromEntity)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(new org.springframework.data.domain.PageImpl<>(dtos, pageable, manutencoes.size()));
        }
        
        Page<Manutencao> manutencoes = manutencaoService.listar(pageable);
        Page<ManutencaoResponseDto> dtos = manutencoes.map(ManutencaoResponseDto::fromEntity);
        return ResponseEntity.ok(dtos);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar manutenção", description = "Remove um registro de manutenção")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Manutenção deletada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Manutenção não encontrada")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Integer id) {
        manutencaoService.deletar(id);
    }
}

