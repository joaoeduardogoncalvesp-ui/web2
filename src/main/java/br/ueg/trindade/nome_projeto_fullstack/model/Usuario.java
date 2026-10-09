package br.ueg.trindade.nome_projeto_fullstack.model;

import jakarta.persistence.*;

@Entity @Table(name = "usuarios", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class Usuario {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) public Long id;
    @Column(nullable = false) public String nome;
    @Column(nullable = false) public String email;
    @Column(unique = true) public String username;
    @com.fasterxml.jackson.annotation.JsonIgnore
    @Column(nullable = false) public String senha;
    @Enumerated(EnumType.STRING) @Column(nullable = false) public Papel papel = Papel.USER;
    public enum Papel { USER, ADMIN }
    protected Usuario() {}
    public Usuario(String nome, String username, String email, String senha, Papel papel) {
        this.nome = nome; this.username = username; this.email = email; this.senha = senha; this.papel = papel;
    }
}
