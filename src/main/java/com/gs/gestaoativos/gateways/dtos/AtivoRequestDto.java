package com.gs.gestaoativos.gateways.dtos;

import com.gs.gestaoativos.domains.Ativo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AtivoRequestDto {

    private Integer idAtivo;

    @NotBlank(message = "Marca é obrigatória")
    @Size(max = 50, message = "Marca deve ter no máximo 50 caracteres")
    private String marca;

    @Size(max = 50, message = "Modelo deve ter no máximo 50 caracteres")
    private String modelo;

    @Size(max = 50, message = "Número de série deve ter no máximo 50 caracteres")
    private String numeroSerie;

    @NotBlank(message = "Status é obrigatório")
    @Size(max = 3, message = "Status deve ter no máximo 3 caracteres")
    private String status;

    @NotNull(message = "Data de aquisição é obrigatória")
    private LocalDate dataAquisicao;

    private LocalDate dataUltAtualizacao;

    @NotNull(message = "Categoria é obrigatória")
    private Integer categoriaIdCateg;

    public Ativo toEntity() {
        return Ativo.builder()
                .idAtivo(this.idAtivo)
                .marca(this.marca)
                .modelo(this.modelo)
                .numeroSerie(this.numeroSerie)
                .status(this.status)
                .dataAquisicao(this.dataAquisicao)
                .dataUltAtualizacao(this.dataUltAtualizacao)
                .build();
    }
}

