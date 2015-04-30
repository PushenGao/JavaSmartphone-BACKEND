package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;

public class BInaryUpload {
	private static URI getBaseURI() {
        return UriBuilder.fromUri("http://localhost:8080/Jersey/rest/werun").build("");
    }
	
	public static void main(String[] args) throws FileNotFoundException{
		ClientConfig config = new DefaultClientConfig();
        Client client = Client.create(config);

        WebResource resource = client.resource(getBaseURI()).path("uploadStream/fucke/lover/delete");
        InputStream fileInStream = new FileInputStream("abc");

		  
		ClientResponse response = resource.type(MediaType.APPLICATION_OCTET_STREAM)
		                        .post(ClientResponse.class, fileInStream);
        System.out.println("Response: " + response.getClientResponseStatus());
	}

}
