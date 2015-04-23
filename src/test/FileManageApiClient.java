package test;

import java.io.File;
import java.net.URI;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.multipart.BodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;

public class FileManageApiClient {

    
    private static URI getBaseURI() {
        return UriBuilder.fromUri("http://localhost:8080/Jersey/rest/werun").build("");
    }
    
    public static void main(String[] args){
    	final ClientConfig config = new DefaultClientConfig();
        final Client client = Client.create(config);

        final WebResource resource = client.resource(getBaseURI()).path("upload/lover/add");
        
        BodyPart part1=new BodyPart();
        part1.setMediaType(MediaType.TEXT_PLAIN_TYPE);
        part1.getHeaders().add("Content-Disposition","form-data; name=\"senderId\"");
        part1.setEntity("fucker");
        part1.getHeaders().add("Content-Disp","form-data; name=\"timestamp\"");
        part1.setEntity("fucker");
        
        final File fileToUpload = new File("/home/pushen/imageToUpload.png");
        final FormDataMultiPart multiPart = new FormDataMultiPart();
        if (fileToUpload != null) {
            multiPart.bodyPart(new FileDataBodyPart("file", fileToUpload,
                    MediaType.APPLICATION_OCTET_STREAM_TYPE))
                    .bodyPart(part1);
        }

        final ClientResponse clientResp = resource.type(
                MediaType.MULTIPART_FORM_DATA_TYPE).post(ClientResponse.class,
                multiPart);
        System.out.println("Response: " + clientResp.getClientResponseStatus());

        client.destroy();
    }
}
