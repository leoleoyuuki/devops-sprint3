package br.com.fiap.challenge3.model;

import jakarta.persistence.*;
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
@Table(name = "HistoricoInteracoes")
public class HistoricoInteracoes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "A proposta negocio não pode ser nulo")
    private String propostaNegocio;

    @Enumerated(EnumType.STRING)
    private OpcoesContrato contratoAssinado;

    @NotNull(message = "O feedback sobre os serviços e produtos não pode ser nulo")
    private String feedbackServicosProdutos;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_empresa")
    private Empresas empresa;
}