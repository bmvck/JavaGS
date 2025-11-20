package com.gs.gestaoativos.gateways.dtos;

import com.gs.gestaoativos.domains.Historico;
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
public class HistoricoRequestDto {

    private Integer idHistorico;

    private LocalDate dataMovimentacao;

    @NotBlank(message = "Tipo de movimentação é obrigatório")
    @Size(max = 50, message = "Tipo de movimentação deve ter no máximo 50 caracteres")
    private String tipoMovimentacao;

    @Size(max = 200, message = "Descrição deve ter no máximo 200 caracteres")
    private String descricaoMoviment;

    @NotNull(message = "Ativo é obrigatório")
    private Integer ativoIdAtivo;

    @NotNull(message = "Colaborador é obrigatório")
    private Long colaboradorIdColab;

    public Historico toEntity() {
        return Historico.builder()
                .idHistorico(this.idHistorico)
                .dataMovimentacao(this.dataMovimentacao != null ? this.dataMovimentacao : LocalDate.now())
                .tipoMovimentacao(this.tipoMovimentacao)
                .descricaoMoviment(this.descricaoMoviment)
                .build();
    }
}

