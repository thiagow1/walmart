package br.com.walmart.logistica.model.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.com.walmart.logistica.model.entity.Ponto;

public class RotaEntregaVO implements Serializable{
	
	private static final long serialVersionUID = 7071975024652798908L;
	
	private List<Ponto> listaPonto;
	private Double distancia;
	private Double custo;
	
	public List<Ponto> getListaPonto() {
		return listaPonto;
	}
	public void setListaPonto(List<Ponto> listaPonto) {
		this.listaPonto = listaPonto;
	}
	public void addPonto(Ponto ponto) {
		if(this.listaPonto == null) {
			this.listaPonto = new ArrayList<Ponto>();
		}
		this.listaPonto.add(0, ponto);
	}
	
	public Double getDistancia() {
		return distancia;
	}
	public void setDistancia(Double distancia) {
		this.distancia = distancia;
	}
	public Double getCusto() {
		return custo;
	}
	public void setCusto(Double custo) {
		this.custo = custo;
	}
	public String getRetorno() {
		StringBuilder sb = new StringBuilder();
		for(Ponto ponto : listaPonto) {
			sb.append(ponto.getNome());
			sb.append(" ");
		}
		sb.append(getCusto());
		return sb.toString();
	}
}
