package com.gs.gestaoativos.gateways.controllers;

import com.gs.gestaoativos.domains.Historico;
import com.gs.gestaoativos.gateways.dtos.HistoricoRequestDto;
import com.gs.gestaoativos.gateways.dtos.HistoricoResponseDto;
import com.gs.gestaoativos.services.HistoricoService;
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
@RequestMapping("/api/historicos")
@RequiredArgsConstructor
@Tag(name = "Históricos", description = "Endpoints para gerenciamento de histórico de movimentações")
public class HistoricoController {

    private final HistoricoService historicoService;

    @PostMapping
    @Operation(summary = "Criar histórico", description = "Cria um novo registro de histórico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Histórico criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @ResponseStatus(HttpStatus.CREATED)
    public HistoricoResponseDto criar(@Valid @RequestBody HistoricoRequestDto dto) {
        Historico historico = historicoService.criar(dto.toEntity(), dto.getAtivoIdAtivo(), dto.getColaboradorIdColab());
        return HistoricoResponseDto.fromEntity(historico);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar histórico", description = "Atualiza um registro de histórico existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Histórico atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Histórico não encontrado")
    })
    public HistoricoResponseDto atualizar(@PathVariable Integer id, @Valid @RequestBody HistoricoRequestDto dto) {
        Historico historico = historicoService.atualizar(id, dto.toEntity(), dto.getAtivoIdAtivo(), dto.getColaboradorIdColab());
        return HistoricoResponseDto.fromEntity(historico);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar histórico por ID", description = "Retorna um registro de histórico específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Histórico encontrado"),
            @ApiResponse(responseCode = "404", description = "Histórico não encontrado")
    })
    public HistoricoResponseDto buscarPorId(@PathVariable Integer id) {
        Historico historico = historicoService.buscarPorId(id);
        return HistoricoResponseDto.fromEntity(historico);
    }

    @GetMapping
    @Operation(summary = "Listar históricos", description = "Retorna uma lista paginada de históricos com filtros opcionais. Campos válidos para ordenação: dataMovimentacao, tipoMovimentacao")
    public ResponseEntity<Page<HistoricoResponseDto>> listar(
            @ParameterObject @PageableDefault(size = 10, sort = "dataMovimentacao", direction = Sort.Direction.DESC) Pageable pageable,
            @Parameter(description = "Filtrar por tipo de movimentação") @RequestParam(required = false) String tipoMovimentacao,
            @Parameter(description = "Filtrar por ID do ativo") @RequestParam(required = false) Integer ativoId,
            @Parameter(description = "Filtrar por ID do colaborador") @RequestParam(required = false) Long colaboradorId,
            @Parameter(description = "Filtrar por data de início") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @Parameter(description = "Filtrar por data de fim") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        
        if (tipoMovimentacao != null || ativoId != null || colaboradorId != null || dataInicio != null || dataFim != null) {
            List<Historico> historicos = historicoService.buscarPorFiltros(tipoMovimentacao, ativoId, colaboradorId, dataInicio, dataFim);
            List<HistoricoResponseDto> dtos = historicos.stream()
                    .map(HistoricoResponseDto::fromEntity)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(new org.springframework.data.domain.PageImpl<>(dtos, pageable, historicos.size()));
        }
        
        Page<Historico> historicos = historicoService.listar(pageable);
        Page<HistoricoResponseDto> dtos = historicos.map(HistoricoResponseDto::fromEntity);
        return ResponseEntity.ok(dtos);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar histórico", description = "Remove um registro de histórico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Histórico deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Histórico não encontrado")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Integer id) {
        historicoService.deletar(id);
    }
}

