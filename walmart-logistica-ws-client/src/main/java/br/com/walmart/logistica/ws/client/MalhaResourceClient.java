package br.com.walmart.logistica.ws.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

/**
 * Classe responsável por efetuar chamadas do WebService da Malha.
 * 
 * @author Thiago
 */
public class MalhaResourceClient {
	private static final Logger LOGGER = LogManager.getLogger(MalhaResourceClient.class);
	
	public static void main(String[] args) {
		LOGGER.info("Incluindo nova malha");
		
		Client client = Client.create();
		
		StringBuilder sbInput = new StringBuilder();
		sbInput.append("{ ");
		sbInput.append("\"nome\": \"TESTE\", ");
		sbInput.append("\"rotas\": [");
		sbInput.append("\"A B 10\",");
		sbInput.append("\"B D 15\",");
		sbInput.append("\"A C 20\",");
		sbInput.append("\"C D 30\",");
		sbInput.append("\"B E 50\",");
		sbInput.append("\"D E 30\"]");
		sbInput.append("}");
		
		LOGGER.info(sbInput);
		
		WebResource webResource = client.resource("http://localhost:8080/walmart-logistica-ws/rest/malha/");
		ClientResponse response = webResource.type("application/json").post(ClientResponse.class, sbInput.toString());
		
		String output = response.getEntity(String.class);		
		if(response.getStatus() == 200) {
			LOGGER.info(output);
		} else {
			LOGGER.error(output);
		}
		
		LOGGER.info("Consultando uma Rota");
		String input = "TESTE/A/E/10/2.5";
		
		LOGGER.info(input);
		
		webResource = client.resource("http://localhost:8080/walmart-logistica-ws/rest/malha/calcularRota/" + input);		
		response = webResource.accept("application/json").get(ClientResponse.class);
		
		output = response.getEntity(String.class);		
		if(response.getStatus() == 200) {
			LOGGER.info(output);
		} else {
			LOGGER.error(output);
		}
		
	}
}
