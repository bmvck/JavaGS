package com.gs.gestaoativos.domains;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "colaborador")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "idColab")
public class Colaborador {

    @Id
    @Column(name = "id_colab", precision = 10, scale = 0)
    private Long idColab;

    @NotBlank(message = "Nome do colaborador é obrigatório")
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    @Column(name = "nome_colab", nullable = false, length = 100)
    private String nomeColab;

    @Size(max = 11, message = "CPF deve ter no máximo 11 caracteres")
    @Column(name = "cpf_colab", length = 11, columnDefinition = "CHAR(11)")
    private String cpfColab;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    @Size(max = 80, message = "Email deve ter no máximo 80 caracteres")
    @Column(name = "email_colab", nullable = false, length = 80)
    private String emailColab;

    @Size(max = 16, message = "Telefone deve ter no máximo 16 caracteres")
    @Column(name = "tel_colab", length = 16)
    private String telColab;

    @NotBlank(message = "Status é obrigatório")
    @Size(max = 3, message = "Status deve ter no máximo 3 caracteres")
    @Column(name = "status_colab", nullable = false, length = 3, columnDefinition = "CHAR(3)")
    @Builder.Default
    private String statusColab = "ATV";

    @Size(max = 30, message = "Função deve ter no máximo 30 caracteres")
    @Column(name = "funcao_colab", length = 30)
    private String funcaoColab;

    @Size(max = 50, message = "Área deve ter no máximo 50 caracteres")
    @Column(name = "area_colab", length = 50)
    private String areaColab;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsavel")
    private Colaborador responsavel;

    @OneToMany(mappedBy = "responsavel", fetch = FetchType.LAZY)
    private List<Colaborador> subordinados;

    @Size(max = 100, message = "Empresa deve ter no máximo 100 caracteres")
    @Column(length = 100)
    private String empresa;

    @Size(max = 4, message = "Ramal interno deve ter no máximo 4 caracteres")
    @Column(name = "ramal_interno", length = 4, columnDefinition = "CHAR(4)")
    private String ramalInterno;

    @Column(length = 255)
    private String senha;

    @Size(max = 20, message = "Role deve ter no máximo 20 caracteres")
    @Column(length = 20)
    @Builder.Default
    private String role = "USER";

    @OneToMany(mappedBy = "colaborador", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Emprestimo> emprestimos;

    @OneToMany(mappedBy = "colaborador", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Historico> historicos;
}

