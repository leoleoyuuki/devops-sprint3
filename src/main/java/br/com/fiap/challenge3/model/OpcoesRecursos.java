package br.com.fiap.challenge3.model;

public enum OpcoesRecursos {

    SIM("Sim"),
    NAO("Não");

    private final String descricao;

    OpcoesRecursos(String descricao) {
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
