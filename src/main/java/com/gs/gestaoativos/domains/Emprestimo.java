package com.gs.gestaoativos.domains;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "emprestimo")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "idEmprestimo")
public class Emprestimo {

    @Id
    @Column(name = "id_emprestimo")
    private Integer idEmprestimo;

    @NotNull(message = "Data de empréstimo é obrigatória")
    @Column(name = "data_emprestimo", nullable = false)
    private LocalDate dataEmprestimo;

    @Column(name = "data_devolucao")
    private LocalDate dataDevolucao;

    @NotBlank(message = "Status do empréstimo é obrigatório")
    @Size(max = 30, message = "Status deve ter no máximo 30 caracteres")
    @Column(name = "status_emprestimo", nullable = false, length = 30)
    private String statusEmprestimo;

    @NotNull(message = "Ativo é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ativo_id_ativo", nullable = false)
    private Ativo ativo;

    @NotNull(message = "Colaborador é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "colaborador_id_colab", nullable = false)
    private Colaborador colaborador;
}

