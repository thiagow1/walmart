package br.com.walmart.logistica.ws.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class MalhaResourceClient {
	private static final Logger LOGGER = LogManager.getLogger(MalhaResourceClient.class);
	
	public static void main(String[] args) {
		LOGGER.info("Incluindo nova malha");
		
		Client client = Client.create();
		
		StringBuilder sbInput = new StringBuilder();
		sbInput.append("{ ");
		sbInput.append("\"nome\": \"TESTE\", ");
		sbInput.append("\"rotas\": \"");
		sbInput.append("A B 10\\n");
		sbInput.append("B D 15\\n");
		sbInput.append("A C 20\\n");
		sbInput.append("C D 30\\n");
		sbInput.append("B E 50\\n");
		sbInput.append("D E 30");
		sbInput.append("\"");
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
		
	}
}
