package br.com.walmart.logistica.service;

import javax.ejb.Local;

import br.com.walmart.logistica.exception.ApplicationException;
import br.com.walmart.logistica.exception.RotaNaoEncontradaException;
import br.com.walmart.logistica.model.vo.MalhaVO;
import br.com.walmart.logistica.model.vo.RotaEntregaVO;
import br.com.walmart.logistica.model.vo.RotaVO;

/**
 * Interface responsável pela camada de serviço da Malha
 * 
 * @author Thiago
 *
 */
@Local
public interface MalhaService {
	public void carregaMalha(MalhaVO malhaVO) throws ApplicationException;
	public RotaEntregaVO calcularRota(RotaVO rotaVO) throws ApplicationException, RotaNaoEncontradaException;
}
