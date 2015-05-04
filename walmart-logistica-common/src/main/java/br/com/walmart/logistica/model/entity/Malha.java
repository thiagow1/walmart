package br.com.walmart.logistica.model.entity;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Entity
@Table(name="malha")
@NamedQueries({ 
	@NamedQuery(name = "Malha.findByNome", query = "SELECT m " 
			+ "FROM Malha m " 
			+ "JOIN FETCH m.listaPonto ponto "
			+ "LEFT JOIN FETCH ponto.listaRota rota "
			+ "LEFT JOIN FETCH rota.origem origem "
			+ "LEFT JOIN FETCH rota.destino destino "
			+ "WHERE m.nome = :nome")})
public class Malha implements Serializable {
	private static final long serialVersionUID = -4132265349079606589L;
	
	public static final String FIND_BY_NOME = "Malha.findByNome";
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_malha")
	private Long id;
	
	@Column(name = "nome")
	private String nome;
	
	@OrderBy("id")
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "id_malha")
	private Set<Ponto> listaPonto;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public Set<Ponto> getListaPonto() {
		return listaPonto;
	}
	public void setListaPonto(Set<Ponto> listaPonto) {
		this.listaPonto = listaPonto;
	}
	public void addPonto(Ponto ponto) {
		if(listaPonto == null) {
			listaPonto = new LinkedHashSet<Ponto>();
		}
		
		listaPonto.add(ponto);
	}
	
	public Ponto findPontoByNome(String nome) {
		for(Ponto ponto : listaPonto) {
			if(nome.equals(ponto.getNome())) {
				return ponto;
			}	
		}
		return null;
	}
}
