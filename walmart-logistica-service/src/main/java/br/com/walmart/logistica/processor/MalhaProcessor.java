package br.com.walmart.logistica.processor;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import br.com.walmart.logistica.exception.RotaNaoEncontradaException;
import br.com.walmart.logistica.exception.ValidationException;
import br.com.walmart.logistica.model.entity.Malha;
import br.com.walmart.logistica.model.entity.Ponto;
import br.com.walmart.logistica.model.entity.Rota;
import br.com.walmart.logistica.model.vo.MalhaVO;
import br.com.walmart.logistica.model.vo.RotaEntregaVO;
import br.com.walmart.logistica.model.vo.RotaVO;

/**
 * Classe responsável por realizar a carga e calcular a rota mais próxima de uma determinada malha.
 * 
 * @author Thiago
 *
 */
public class MalhaProcessor {
	
	private void validaMalha(MalhaVO malhaVO) throws ValidationException {
		StringBuilder sbMessage = new StringBuilder();

		if (malhaVO.getNome() == null || "".equals(malhaVO.getNome())) {
			sbMessage.append("O campo 'Nome' é obrigatório!\n");
		}
		if (malhaVO.getRotas() == null || "".equals(malhaVO.getRotas())) {
			sbMessage.append("O campo 'Rotas' é obrigatório!\n");
		}

		if (sbMessage.length() > 0) {
			throw new ValidationException(sbMessage.toString());
		}
	}
	
	public Malha carregaMalha(MalhaVO malhaVO) throws ValidationException {
		validaMalha(malhaVO);

		Malha malha = new Malha();
		malha.setNome(malhaVO.getNome());

		Map<String, Ponto> mapaPonto = new HashMap<String, Ponto>();

		for (String linha : malhaVO.getRotas()) {
			String[] campos = linha.split("\\s+");
			String nomeOrigem = campos[0];
			String nomeDestino = campos[1];
			Double distancia = Double.valueOf(campos[2]);

			Ponto origem = carregaPonto(nomeOrigem, malha, mapaPonto);

			Ponto destino = carregaPonto(nomeDestino, malha, mapaPonto);

			Rota rota = new Rota();
			rota.setOrigem(origem);
			rota.setDestino(destino);
			rota.setDistancia(distancia);

			origem.addRota(rota);
		}

		for (Entry<String, Ponto> entryPonto : mapaPonto.entrySet()) {
			malha.addPonto(entryPonto.getValue());
		}

		return malha;
	}

	private Ponto carregaPonto(String nome, Malha malha, Map<String, Ponto> mapaPonto) {
		Ponto ponto = null;

		if (mapaPonto.containsKey(nome)) {
			ponto = mapaPonto.get(nome);
		} else {
			ponto = new Ponto();
			ponto.setMalha(malha);
			ponto.setNome(nome);
			mapaPonto.put(nome, ponto);
		}

		return ponto;
	}
	
	public RotaEntregaVO calcularRota(Malha malha, RotaVO rotaVO) throws RotaNaoEncontradaException {
		Ponto pontoOrigem = malha.findPontoByNome(rotaVO.getOrigem());
		Ponto pontoDestino = malha.findPontoByNome(rotaVO.getDestino());

		Set<Ponto> listaPontoVisitado = new LinkedHashSet<Ponto>(malha.getListaPonto());
		Map<Ponto, Double> mapDistancia = new HashMap<Ponto, Double>();
		for (Ponto ponto : malha.getListaPonto()) {
			mapDistancia.put(ponto, Double.MAX_VALUE);
		}

		Map<Ponto, Ponto> mapPontos = new HashMap<Ponto, Ponto>();

		listaPontoVisitado.remove(pontoOrigem);
		mapDistancia.put(pontoOrigem, 0d);
		mapPontos.put(pontoDestino, null);

		// Realiza o calculo das distancias usando o algoritmo de Dijkstra
		calcularDistancias(pontoOrigem, mapDistancia, mapPontos);
		Ponto ponto = null;
		do {
			ponto = buscaPontoMaisPerto(mapDistancia, listaPontoVisitado);

			if (ponto != null && ponto.getListaRota() != null) {
				calcularDistancias(ponto, mapDistancia, mapPontos);
				listaPontoVisitado.remove(ponto);
			}
		} while (ponto != null && ponto.getListaRota() != null && !listaPontoVisitado.isEmpty());

		// Monta as rotas para o retorno
		RotaEntregaVO rotaEntregaVO = new RotaEntregaVO();
		Double distancia = mapDistancia.get(pontoDestino);
		
		if(distancia == null) {
			throw new RotaNaoEncontradaException();
		}
		
		rotaEntregaVO.setDistancia(distancia);
		Ponto precedente = pontoDestino;
		do {
			rotaEntregaVO.addPonto(precedente);
			precedente = mapPontos.get(precedente);
		} while (precedente != null);

		rotaEntregaVO.setCusto((rotaEntregaVO.getDistancia() / rotaVO.getAutonomia()) * rotaVO.getValor());

		return rotaEntregaVO;
	}

	private void calcularDistancias(Ponto ponto, Map<Ponto, Double> mapDistancia, Map<Ponto, Ponto> mapPontos) {

		for (Rota rota : ponto.getListaRota()) {
			Ponto outroPonto = rota.getDestino();

			Double distanciaAtual = mapDistancia.get(ponto) + rota.getDistancia();
			Double distanciaAnterior = mapDistancia.get(outroPonto);

			if (distanciaAtual < distanciaAnterior) {
				mapDistancia.put(outroPonto, distanciaAtual);
				mapPontos.put(outroPonto, ponto);
			}
		}
	}

	private Ponto buscaPontoMaisPerto(Map<Ponto, Double> mapDistancia, Set<Ponto> listaPontoVisitado) {
		double maiorDistancia = Double.MAX_VALUE;
		Ponto ponto = null;
		for (Ponto visitado : listaPontoVisitado) {
			double distancia = mapDistancia.get(visitado);
			if (distancia < maiorDistancia) {
				maiorDistancia = distancia;
				ponto = visitado;
			}
		}
		return ponto;
	}
}
