package br.com.fiap.challenge3.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "Empresas")
public class Empresas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 4, max = 255, message = "O nome da nova empresa deve ter, ao menos, 4 caracteres e, no máximo, 255")
    @NotEmpty(message = "O nome não pode estar vazio")
    private String nome;

    @Enumerated(EnumType.STRING)
    private OpcoesTamanho tamanho;

    @Enumerated(EnumType.STRING)
    private OpcoesSetor setor;

    @NotEmpty(message = "A localização geográfica não pode estar vazia")
    private String localizacaoGeografica;

    @PositiveOrZero(message = "O número de funcionários ser zero ou positivo")
    @NotNull(message = "O número de funcionários não pode ser nulo")
    private Integer numeroFuncionarios;

    @Enumerated(EnumType.STRING)
    private OpcoesTipo tipoEmpresa;

    private Boolean cliente;

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TendenciasGastos> tendencias = new ArrayList<>();

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DesempenhoFinanceiro> desempenhos = new ArrayList<>();

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HistoricoInteracoes> historicos;

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ComportamentoNegocios> comportamentos = new ArrayList<>();
}
