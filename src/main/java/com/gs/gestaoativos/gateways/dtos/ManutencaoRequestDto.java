package com.gs.gestaoativos.gateways.dtos;

import com.gs.gestaoativos.domains.Manutencao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManutencaoRequestDto {

    private Integer idManutencao;

    @NotBlank(message = "Tipo de manutenção é obrigatório")
    @Size(max = 30, message = "Tipo de manutenção deve ter no máximo 30 caracteres")
    private String tipoManutencao;

    @NotNull(message = "Data de início é obrigatória")
    private LocalDate dataInicio;

    private LocalDate dataFim;

    private BigDecimal custo;

    @Size(max = 200, message = "Descrição deve ter no máximo 200 caracteres")
    private String descricao;

    @NotNull(message = "Ativo é obrigatório")
    private Integer ativoIdAtivo;

    public Manutencao toEntity() {
        return Manutencao.builder()
                .idManutencao(this.idManutencao)
                .tipoManutencao(this.tipoManutencao)
                .dataInicio(this.dataInicio)
                .dataFim(this.dataFim)
                .custo(this.custo)
                .descricao(this.descricao)
                .build();
    }
}

