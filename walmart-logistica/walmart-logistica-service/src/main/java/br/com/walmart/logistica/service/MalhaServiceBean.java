package br.com.walmart.logistica.service;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import br.com.walmart.logistica.exception.ApplicationException;
import br.com.walmart.logistica.exception.ValidationException;
import br.com.walmart.logistica.model.entity.Malha;
import br.com.walmart.logistica.model.entity.Ponto;
import br.com.walmart.logistica.model.entity.Rota;
import br.com.walmart.logistica.model.vo.MalhaVO;
import br.com.walmart.logistica.model.vo.RotaEntregaVO;
import br.com.walmart.logistica.model.vo.RotaVO;

@Stateless
public class MalhaServiceBean implements MalhaService {

	@PersistenceContext
	private EntityManager em;

	private static final Pattern MALHA_PATTERN = Pattern.compile("(.\\s+.\\s+\\d+\\s*\n?)+");

	private void validaMalha(MalhaVO malhaVO) throws ValidationException {
		StringBuilder sbMessage = new StringBuilder();

		if (malhaVO.getNome() == null || "".equals(malhaVO.getNome())) {
			sbMessage.append("O campo 'Nome' é obrigatório!\n");
		}
		if (malhaVO.getRotas() == null || "".equals(malhaVO.getRotas())) {
			sbMessage.append("O campo 'Rotas' é obrigatório!\n");
		}
		if (!MALHA_PATTERN.matcher(malhaVO.getRotas()).matches()) {
			sbMessage.append("O formato da malha logística é inválido!\n");
		}

		if (sbMessage.length() > 0) {
			throw new ValidationException(sbMessage.toString());
		}
	}

	@Override
	public void carregaMalha(MalhaVO malhaVO) throws ApplicationException {
		try {
			validaMalha(malhaVO);

			Malha malha = new Malha();
			malha.setNome(malhaVO.getNome());

			Map<String, Ponto> mapaPonto = new HashMap<String, Ponto>();

			String[] linhas = malhaVO.getRotas().split("\n");

			for (String linha : linhas) {
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

			save(malha);
		} catch (Exception e) {
			throw new ApplicationException(e);
		}
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

	private void save(Malha malha) throws ApplicationException {
		em.persist(malha);
		em.flush();
	}

	public RotaEntregaVO calcularRota(RotaVO rotaVO) throws ApplicationException {

		try {
			Malha malha = findByNome(rotaVO.getMalha());

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

				if (ponto != null) {
					calcularDistancias(ponto, mapDistancia, mapPontos);
					listaPontoVisitado.remove(ponto);
				}
			} while (ponto != null && !listaPontoVisitado.isEmpty());

			// Monta as rotas para o retorno
			RotaEntregaVO rotaEntregaVO = new RotaEntregaVO();
			rotaEntregaVO.setDistancia(mapDistancia.get(pontoDestino));
			Ponto precedente = pontoDestino;
			do {
				rotaEntregaVO.addPonto(precedente);
				precedente = mapPontos.get(precedente);
			} while (precedente != null);

			rotaEntregaVO.setCusto((rotaEntregaVO.getDistancia() / rotaVO.getAutonomia()) * rotaVO.getValor());

			return rotaEntregaVO;

		} catch (Exception e) {
			throw new ApplicationException(e);
		}
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

	private Malha findByNome(String nome) throws ApplicationException {
		try {
			TypedQuery<Malha> query = em.createNamedQuery(Malha.FIND_BY_NOME, Malha.class);
			query.setParameter("nome", nome);

			return query.getSingleResult();
		} catch (Exception e) {
			throw new ApplicationException(e);
		}
	}
}
