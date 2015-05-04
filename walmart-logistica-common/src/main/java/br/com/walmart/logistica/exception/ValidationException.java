package br.com.walmart.logistica.exception;

/**
 * Classe responsável por tratar exceção quando ocorrer um erro de validação
 * 
 * @author Thiago
 *
 */
public class ValidationException extends Exception {

	private static final long serialVersionUID = -7174667137276257041L;

	public ValidationException() {
		super();
	}
	
	public ValidationException(String mensagem) {
		super(mensagem);
	}
	
	public ValidationException(Throwable e) {
		super(e);
	}

}