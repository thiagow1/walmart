package br.com.walmart.logistica.model.vo;

import java.io.Serializable;

public class MalhaVO implements Serializable{
	
	private static final long serialVersionUID = -2824663314768464371L;
	
	private String nome;
	private String rotas;
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getRotas() {
		return rotas;
	}
	public void setRotas(String rotas) {
		this.rotas = rotas;
	}
}
