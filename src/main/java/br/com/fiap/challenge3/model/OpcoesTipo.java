package br.com.fiap.challenge3.model;

public enum OpcoesTipo {
    SOCIEDADE_EMPRESARIA_LIMITADA("Sociedade Empresária Limitada"),
    MICROEMPREENDEDOR_INDIVIDUAL("Microempreendedor Individual"),
    SOCIEDADE_LIMITADA_UNIPESSOAL("Sociedade Limitada Unipessoal"),
    SOCIEDADE_ANONIMA("Sociedade Anônima"),
    EMPRESARIO_INDIVIDUAL("Empresário Individual"),
    SOCIEDADE_SIMPLES("Sociedade Simples");

    private final String descricao;

    OpcoesTipo(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}

