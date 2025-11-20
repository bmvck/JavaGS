package com.gs.gestaoativos.domains;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "historico")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "idHistorico")
public class Historico {

    @Id
    @Column(name = "id_historico")
    private Integer idHistorico;

    @Column(name = "data_movimentacao", nullable = false)
    @Builder.Default
    private LocalDate dataMovimentacao = LocalDate.now();

    @NotBlank(message = "Tipo de movimentação é obrigatório")
    @Size(max = 50, message = "Tipo de movimentação deve ter no máximo 50 caracteres")
    @Column(name = "tipo_movimentacao", nullable = false, length = 50)
    private String tipoMovimentacao;

    @Size(max = 200, message = "Descrição deve ter no máximo 200 caracteres")
    @Column(name = "descricao_moviment", length = 200)
    private String descricaoMoviment;

    @NotNull(message = "Ativo é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ativo_id_ativo", nullable = false)
    private Ativo ativo;

    @NotNull(message = "Colaborador é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "colaborador_id_colab", nullable = false)
    private Colaborador colaborador;
}

