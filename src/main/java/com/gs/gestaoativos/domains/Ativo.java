package com.gs.gestaoativos.domains;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "ativo")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "idAtivo")
public class Ativo {

    @Id
    @Column(name = "id_ativo")
    private Integer idAtivo;

    @NotBlank(message = "Marca é obrigatória")
    @Size(max = 50, message = "Marca deve ter no máximo 50 caracteres")
    @Column(nullable = false, length = 50)
    private String marca;

    @Size(max = 50, message = "Modelo deve ter no máximo 50 caracteres")
    @Column(length = 50)
    private String modelo;

    @Size(max = 50, message = "Número de série deve ter no máximo 50 caracteres")
    @Column(name = "numero_serie", length = 50)
    private String numeroSerie;

    @NotBlank(message = "Status é obrigatório")
    @Size(max = 3, message = "Status deve ter no máximo 3 caracteres")
    @Column(name = "status", nullable = false, length = 3, columnDefinition = "CHAR(3)")
    private String status;

    @NotNull(message = "Data de aquisição é obrigatória")
    @Column(name = "data_aquisicao", nullable = false)
    private LocalDate dataAquisicao;

    @Column(name = "data_ult_atualizacao")
    private LocalDate dataUltAtualizacao;

    @NotNull(message = "Categoria é obrigatória")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id_categ", nullable = false)
    private Categoria categoria;

    @OneToMany(mappedBy = "ativo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Emprestimo> emprestimos;

    @OneToMany(mappedBy = "ativo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Historico> historicos;

    @OneToMany(mappedBy = "ativo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Manutencao> manutencoes;

    @OneToMany(mappedBy = "ativo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DispositivoIoT> dispositivosIoT;
}

