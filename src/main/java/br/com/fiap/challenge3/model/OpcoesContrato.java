package br.com.fiap.challenge3.model;

public enum OpcoesContrato {
    SIM("Sim"),
    NAO("Não");

    private final String descricao;

    OpcoesContrato(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}


