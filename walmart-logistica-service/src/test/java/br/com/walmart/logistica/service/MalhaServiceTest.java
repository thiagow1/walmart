package br.com.walmart.logistica.service;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import br.com.walmart.logistica.exception.RotaNaoEncontradaException;
import br.com.walmart.logistica.exception.ValidationException;
import br.com.walmart.logistica.model.entity.Malha;
import br.com.walmart.logistica.model.vo.MalhaVO;
import br.com.walmart.logistica.model.vo.RotaEntregaVO;
import br.com.walmart.logistica.model.vo.RotaVO;
import br.com.walmart.logistica.processor.MalhaProcessor;

public class MalhaServiceTest {

	@Test
	public void testCalculoRota1() throws ValidationException, RotaNaoEncontradaException {
		MalhaProcessor malhaProcessor = new MalhaProcessor();
		MalhaVO malhaVO = new MalhaVO();
		malhaVO.setNome("TESTE");
		
		List<String> rotas = new ArrayList<String>();
		rotas.add("A B 10");
		rotas.add("B D 15");
		rotas.add("A C 20");
		rotas.add("C D 30");
		rotas.add("B E 50");
		rotas.add("D E 30");
		
		malhaVO.setRotas(rotas);
		
		Malha malha = malhaProcessor.carregaMalha(malhaVO);
		
		RotaVO rotaVO = new RotaVO();
		rotaVO.setOrigem("A");
		rotaVO.setDestino("D");
		rotaVO.setAutonomia(10d);
		rotaVO.setValor(2.5d);
		
		RotaEntregaVO rotaEntrega = malhaProcessor.calcularRota(malha, rotaVO);
		
		assertEquals(rotaEntrega.getRetorno(), "A B D 6.25");
		
	}
	
	@Test
	public void testCalculoRota2() throws ValidationException, RotaNaoEncontradaException {
		MalhaProcessor malhaProcessor = new MalhaProcessor();
		MalhaVO malhaVO = new MalhaVO();
		malhaVO.setNome("TESTE");
		
		List<String> rotas = new ArrayList<String>();
		rotas.add("A B 10");
		rotas.add("B D 15");
		rotas.add("A C 20");
		rotas.add("C D 30");
		rotas.add("B E 50");
		rotas.add("D E 30");
		
		malhaVO.setRotas(rotas);
		
		Malha malha = malhaProcessor.carregaMalha(malhaVO);
		
		RotaVO rotaVO = new RotaVO();
		rotaVO.setOrigem("A");
		rotaVO.setDestino("E");
		rotaVO.setAutonomia(10d);
		rotaVO.setValor(2.5d);
		
		RotaEntregaVO rotaEntrega = malhaProcessor.calcularRota(malha, rotaVO);
		
		assertEquals(rotaEntrega.getRetorno(), "A B D E 13.75");		
	}
	
	@Test
	public void testCalculoRota3() throws ValidationException, RotaNaoEncontradaException {
		MalhaProcessor malhaProcessor = new MalhaProcessor();
		MalhaVO malhaVO = new MalhaVO();
		malhaVO.setNome("TESTE");
		
		List<String> rotas = new ArrayList<String>();
		rotas.add("A B 10");
		rotas.add("B D 15");
		rotas.add("A C 20");
		rotas.add("C D 30");
		rotas.add("B E 50");
		rotas.add("D E 30");
		
		malhaVO.setRotas(rotas);
		
		Malha malha = malhaProcessor.carregaMalha(malhaVO);
		
		RotaVO rotaVO = new RotaVO();
		rotaVO.setOrigem("A");
		rotaVO.setDestino("E");
		rotaVO.setAutonomia(10d);
		rotaVO.setValor(2.5d);
		
		RotaEntregaVO rotaEntrega = malhaProcessor.calcularRota(malha, rotaVO);
		
		assertEquals(rotaEntrega.getRetorno(), "A C 5.0");		
	}
}
