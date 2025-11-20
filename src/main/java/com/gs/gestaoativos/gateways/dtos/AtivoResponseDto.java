package com.gs.gestaoativos.gateways.dtos;

import com.gs.gestaoativos.domains.Ativo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AtivoResponseDto {
    private Integer idAtivo;
    private String marca;
    private String modelo;
    private String numeroSerie;
    private String status;
    private LocalDate dataAquisicao;
    private LocalDate dataUltAtualizacao;
    private Integer categoriaIdCateg;
    private String categoriaNome;

    public static AtivoResponseDto fromEntity(Ativo ativo) {
        return new AtivoResponseDto(
                ativo.getIdAtivo(),
                ativo.getMarca(),
                ativo.getModelo(),
                ativo.getNumeroSerie(),
                ativo.getStatus(),
                ativo.getDataAquisicao(),
                ativo.getDataUltAtualizacao(),
                ativo.getCategoria() != null ? ativo.getCategoria().getIdCateg() : null,
                ativo.getCategoria() != null ? ativo.getCategoria().getNomeCateg() : null
        );
    }
}

