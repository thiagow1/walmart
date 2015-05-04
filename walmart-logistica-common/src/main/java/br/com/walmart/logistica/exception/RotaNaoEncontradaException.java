package br.com.walmart.logistica.exception;

public class RotaNaoEncontradaException extends Exception {

	private static final long serialVersionUID = 2425981061078440885L;

	public RotaNaoEncontradaException() {
		super("Rota não encontrada");
	}
	
	public RotaNaoEncontradaException(String mensagem) {
		super(mensagem);
	}
	
	public RotaNaoEncontradaException(Throwable e) {
		super(e);
	}

}