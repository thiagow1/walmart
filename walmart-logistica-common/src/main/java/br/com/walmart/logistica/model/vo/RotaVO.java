package br.com.walmart.logistica.model.vo;

import java.io.Serializable;

/**
 * Classe responsável por manter os dados de entrada do WebService calcularRota
 * 
 * @author Thiago
 *
 */
public class RotaVO implements Serializable{
	
	private static final long serialVersionUID = 7071975024652798908L;
	
	private String malha;
	private String origem;
	private String destino;
	private Double autonomia;
	private Double valor;
	
	public String getMalha() {
		return malha;
	}
	public void setMalha(String malha) {
		this.malha = malha;
	}
	public String getOrigem() {
		return origem;
	}
	public void setOrigem(String origem) {
		this.origem = origem;
	}
	public String getDestino() {
		return destino;
	}
	public void setDestino(String destino) {
		this.destino = destino;
	}
	public Double getAutonomia() {
		return autonomia;
	}
	public void setAutonomia(Double autonomia) {
		this.autonomia = autonomia;
	}
	public Double getValor() {
		return valor;
	}
	public void setValor(Double valor) {
		this.valor = valor;
	}
}
