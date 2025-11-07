package com.example.peekaboo;

public class LembreteModel {

    private int id;
    private int userId;
    private int petId;
    private String tipo;
    private String descricao;
    private String data; // DD/MM/AAAA
    private String hora; // HH:MM
    private int ativo; // 1 (ativo) ou 0 (inativo/passado)
    private String petNome;

    // Construtor completo
    public LembreteModel(int id, int userId, int petId, String tipo, String descricao, String data, String hora, int ativo) {
        this.id = id;
        this.userId = userId;
        this.petId = petId;
        this.tipo = tipo;
        this.descricao = descricao;
        this.data = data;
        this.hora = hora;
        this.ativo = ativo;
    }

    // Getters
    public int getId() { return id; }
    public int getUserId() { return userId; }
    public int getPetId() { return petId; }
    public String getTipo() { return tipo; }
    public String getDescricao() { return descricao; }

    public String getData() { return data; }
    public String getHora() { return hora; }
    public int getAtivo() { return ativo; }
    public String getPetNome() { return petNome; }

    public void setPetNome(String petNome) { this.petNome = petNome; }
}