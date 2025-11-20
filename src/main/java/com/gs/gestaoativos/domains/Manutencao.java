package com.gs.gestaoativos.domains;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "manutencao")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "idManutencao")
public class Manutencao {

    @Id
    @Column(name = "id_manutencao")
    private Integer idManutencao;

    @NotBlank(message = "Tipo de manutenção é obrigatório")
    @Size(max = 30, message = "Tipo de manutenção deve ter no máximo 30 caracteres")
    @Column(name = "tipo_manutencao", nullable = false, length = 30)
    private String tipoManutencao;

    @NotNull(message = "Data de início é obrigatória")
    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;

    @Column(name = "data_fim")
    private LocalDate dataFim;

    @Column(precision = 8, scale = 2)
    private BigDecimal custo;

    @Size(max = 200, message = "Descrição deve ter no máximo 200 caracteres")
    @Column(length = 200)
    private String descricao;

    @NotNull(message = "Ativo é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ativo_id_ativo", nullable = false)
    private Ativo ativo;
}

