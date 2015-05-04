package br.com.walmart.logistica.service;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import br.com.walmart.logistica.exception.ApplicationException;
import br.com.walmart.logistica.exception.RotaNaoEncontradaException;
import br.com.walmart.logistica.model.entity.Malha;
import br.com.walmart.logistica.model.vo.MalhaVO;
import br.com.walmart.logistica.model.vo.RotaEntregaVO;
import br.com.walmart.logistica.model.vo.RotaVO;
import br.com.walmart.logistica.processor.MalhaProcessor;

/**
 * Classe responsável por tratar as chamadas de serviço da Malha 
 * 
 * @author Thiago
 */
@Stateless
public class MalhaServiceBean implements MalhaService {

	@PersistenceContext
	private EntityManager em;

	@Override
	public void carregaMalha(MalhaVO malhaVO) throws ApplicationException {
		try {
			MalhaProcessor malhaProcessor = new MalhaProcessor();
			Malha malha = malhaProcessor.carregaMalha(malhaVO);			

			save(malha);
		} catch (Exception e) {
			throw new ApplicationException(e);
		}
	}

	private void save(Malha malha) throws ApplicationException {
		Malha malhaBD = findByNome(malha.getNome());
		
		if(malhaBD != null) {
			em.remove(malhaBD);
		}
		
		em.persist(malha);
		em.flush();
	}

	public RotaEntregaVO calcularRota(RotaVO rotaVO) throws ApplicationException, RotaNaoEncontradaException {

		try {
			Malha malha = findByNome(rotaVO.getMalha());

			MalhaProcessor processor = new MalhaProcessor();
			return processor.calcularRota(malha, rotaVO);
		} catch (RotaNaoEncontradaException e) { 
			throw e;
		} catch (Exception e) {
			throw new ApplicationException(e);
		}
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
