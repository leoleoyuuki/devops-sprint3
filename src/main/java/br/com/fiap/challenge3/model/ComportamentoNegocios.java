package br.com.fiap.challenge3.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "ComportamentoNegocios")
public class ComportamentoNegocios {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "A interação não pode ser nulo")
    private Long interacoesPlataforma;

    @Min(value = 0, message = "A frequência de uso deve ser zero ou positiva")
    @Max(value = 10, message = "A frequência de uso deve ser menor ou igual a 10")
    @NotNull(message = "A frequência de uso não pode ser nulo")
    private Long frequenciaUso;

    @NotNull(message = "O feedback não pode ser nulo")
    private String feedback;

    @Enumerated(EnumType.STRING)
    private OpcoesRecursos usoRecursosEspecificos;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_empresa")
    private Empresas empresa;
}