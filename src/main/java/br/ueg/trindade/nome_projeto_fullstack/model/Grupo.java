package br.ueg.trindade.nome_projeto_fullstack.model;

import jakarta.persistence.*;

@Entity @Table(name = "grupos")
public class Grupo {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) public Long id;
    @Column(nullable = false) public String nome;
    @Column(length = 2000) public String descricao;
    protected Grupo() {}
    public Grupo(String nome, String descricao) { this.nome = nome; this.descricao = descricao; }
}
