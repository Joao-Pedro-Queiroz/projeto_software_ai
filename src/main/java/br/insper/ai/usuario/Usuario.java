package br.insper.ai.usuario;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Usuario {

    private String nome;
    private String email;
    private String papel;

    public Usuario(String nome, String email, String papel) {
        this.nome = nome;
        this.papel = papel;
        this.email = email;
    }

    public Usuario() {

    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPapel() {
        return papel;
    }

    public void setPapel(String papel) {
        this.papel = papel;
    }
}