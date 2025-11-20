package com.gs.gestaoativos.domains;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "categoria")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "idCateg")
public class Categoria {

    @Id
    @Column(name = "id_categ")
    private Integer idCateg;

    @NotBlank(message = "Nome da categoria é obrigatório")
    @Size(max = 50, message = "Nome da categoria deve ter no máximo 50 caracteres")
    @Column(name = "nome_categ", nullable = false, length = 50)
    private String nomeCateg;

    @Size(max = 200, message = "Descrição deve ter no máximo 200 caracteres")
    @Column(name = "desc_categ", length = 200)
    private String descCateg;

    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Ativo> ativos;
}

