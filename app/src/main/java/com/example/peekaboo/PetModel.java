package com.example.peekaboo;

public class PetModel {
    private int petId;
    private String nome;
    private String especie;
    private String dataNasc;
    private String descricao;
    private int userId;

    public PetModel(int petId, String nome, String especie, String dataNasc, String descricao, int userId) {
        this.petId = petId;
        this.nome = nome;
        this.especie = especie;
        this.dataNasc = dataNasc;
        this.descricao = descricao;
        this.userId = userId;
    }

    public int getPetId() {
        return petId;
    }

    public String getNome() {
        return nome;
    }

    public String getEspecie() {
        return especie;
    }

    public String getDataNasc() {
        return dataNasc;
    }

    public String getDescricao() {
        return descricao;
    }

    public int getUserId() {
        return userId;
    }
}