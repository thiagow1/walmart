package br.com.walmart.logistica.exception;

@javax.ejb.ApplicationException(rollback = true)
public class ApplicationException extends Exception {

	private static final long serialVersionUID = 1031473427688830221L;

	public ApplicationException() {
		super();
	}
	
	public ApplicationException(String mensagem) {
		super(mensagem);
	}
	
	public ApplicationException(Throwable e) {
		super(e);
	}

}