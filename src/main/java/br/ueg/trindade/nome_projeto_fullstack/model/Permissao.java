package br.ueg.trindade.nome_projeto_fullstack.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "permissoes")
public class Permissao {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    @Column(nullable = false, unique = true)
    public String nome;
    public String descricao;

    protected Permissao() {}
    public Permissao(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }
}
