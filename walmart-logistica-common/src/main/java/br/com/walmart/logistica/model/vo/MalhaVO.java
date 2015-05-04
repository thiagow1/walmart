package br.com.walmart.logistica.model.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Classe responsável por manter os dados na realização da carga de Malha
 * 
 * @author Thiago
 *
 */
public class MalhaVO implements Serializable{
	
	private static final long serialVersionUID = -2824663314768464371L;
	
	private String nome;
	private List<String> rotas;
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public List<String> getRotas() {
		return rotas;
	}
	public void setRotas(List<String> rotas) {
		this.rotas = rotas;
	}
}
