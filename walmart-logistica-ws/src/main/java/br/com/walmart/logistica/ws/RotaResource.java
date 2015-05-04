package br.com.walmart.logistica.ws;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.walmart.logistica.exception.ApplicationException;
import br.com.walmart.logistica.model.vo.MalhaVO;
import br.com.walmart.logistica.service.MalhaService;

@Stateless
@Path("/rest/malha")
public class RotaResource {

	private static final Logger LOGGER = LogManager.getLogger(RotaResource.class);

	@EJB
	private MalhaService malhaService;

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response incluir(MalhaVO malhaVO) {
		LOGGER.debug("Iniciando incluir(malhaVO)");
		Response response = null;

		try {
			malhaService.carregaMalha(malhaVO);
			response = Response.ok("Malha carregada com sucesso!").build();
		} catch (ApplicationException e) {
			response = Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		} finally {
			LOGGER.debug("Encerrando incluir(malhaVO)");
		}

		return response;
	}
}
