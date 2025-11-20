package com.gs.gestaoativos.gateways.controllers;

import com.gs.gestaoativos.domains.DispositivoIoT;
import com.gs.gestaoativos.gateways.dtos.DispositivoIoTRequestDto;
import com.gs.gestaoativos.gateways.dtos.DispositivoIoTResponseDto;
import com.gs.gestaoativos.services.DispositivoIoTService;
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
@RequestMapping("/api/dispositivos-iot")
@RequiredArgsConstructor
@Tag(name = "Dispositivos IoT", description = "Endpoints para gerenciamento de dispositivos IoT")
public class DispositivoIoTController {

    private final DispositivoIoTService dispositivoIoTService;

    @PostMapping
    @Operation(summary = "Criar dispositivo IoT", description = "Cria um novo dispositivo IoT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Dispositivo IoT criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @ResponseStatus(HttpStatus.CREATED)
    public DispositivoIoTResponseDto criar(@Valid @RequestBody DispositivoIoTRequestDto dto) {
        DispositivoIoT dispositivo = dispositivoIoTService.criar(dto.toEntity(), dto.getAtivoIdAtivo());
        return DispositivoIoTResponseDto.fromEntity(dispositivo);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar dispositivo IoT", description = "Atualiza um dispositivo IoT existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dispositivo IoT atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Dispositivo IoT não encontrado")
    })
    public DispositivoIoTResponseDto atualizar(@PathVariable Integer id, @Valid @RequestBody DispositivoIoTRequestDto dto) {
        DispositivoIoT dispositivo = dispositivoIoTService.atualizar(id, dto.toEntity(), dto.getAtivoIdAtivo());
        return DispositivoIoTResponseDto.fromEntity(dispositivo);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar dispositivo IoT por ID", description = "Retorna um dispositivo IoT específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dispositivo IoT encontrado"),
            @ApiResponse(responseCode = "404", description = "Dispositivo IoT não encontrado")
    })
    public DispositivoIoTResponseDto buscarPorId(@PathVariable Integer id) {
        DispositivoIoT dispositivo = dispositivoIoTService.buscarPorId(id);
        return DispositivoIoTResponseDto.fromEntity(dispositivo);
    }

    @GetMapping
    @Operation(summary = "Listar dispositivos IoT", description = "Retorna uma lista paginada de dispositivos IoT com filtros opcionais. Campos válidos para ordenação: identificadorHw, statusDisp, dataCadastro")
    public ResponseEntity<Page<DispositivoIoTResponseDto>> listar(
            @ParameterObject @PageableDefault(size = 10, sort = "identificadorHw", direction = Sort.Direction.ASC) Pageable pageable,
            @Parameter(description = "Filtrar por identificador de hardware") @RequestParam(required = false) String identificadorHw,
            @Parameter(description = "Filtrar por status") @RequestParam(required = false) String status,
            @Parameter(description = "Filtrar por ID do ativo") @RequestParam(required = false) Integer ativoId) {
        
        if (identificadorHw != null || status != null || ativoId != null) {
            List<DispositivoIoT> dispositivos = dispositivoIoTService.buscarPorFiltros(identificadorHw, status, ativoId);
            List<DispositivoIoTResponseDto> dtos = dispositivos.stream()
                    .map(DispositivoIoTResponseDto::fromEntity)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(new org.springframework.data.domain.PageImpl<>(dtos, pageable, dispositivos.size()));
        }
        
        Page<DispositivoIoT> dispositivos = dispositivoIoTService.listar(pageable);
        Page<DispositivoIoTResponseDto> dtos = dispositivos.map(DispositivoIoTResponseDto::fromEntity);
        return ResponseEntity.ok(dtos);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar dispositivo IoT", description = "Remove um dispositivo IoT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Dispositivo IoT deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Dispositivo IoT não encontrado")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Integer id) {
        dispositivoIoTService.deletar(id);
    }
}

