package org.example;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClients;

import java.io.File;
import java.io.IOException;

public class SwaggerImporter {

    private static final String APIM_HOST = "localhost";
    private static final int APIM_PORT = 9443;

    //private static final String APIM_CLIENT_ID = "<APIM_CLIENT_ID>";
    //private static final String APIM_CLIENT_SECRET = "<APIM_CLIENT_SECRET>";
    private static final String APIM_USERNAME = "admin";
    private static final String APIM_PASSWORD = "admin";

    private static final String IMPORT_API_ENDPOINT = "https://" + APIM_HOST + ":" + APIM_PORT + "/api/am/publisher/v1/apis/import/swagger";
    private static final String TOKEN_API_ENDPOINT = "https://" + APIM_HOST + ":" + APIM_PORT + "/token";

    private static final String ACCESS_TOKEN = "b4ec7fd7-2b8e-3af3-809b-8c0b91f92be9";

    public static void main(String[] args) throws IOException {

        // Create HTTP client
        HttpClient httpClient = HttpClients.createDefault();

        // Import Swagger file
        HttpResponse importResponse = importSwaggerFile(httpClient, ACCESS_TOKEN, new File("C:\\Users\\ambro\\Desktop\\swagger.yaml"), "swagger.yaml");

        // Check if import was successful
        if (importResponse.getStatusLine().getStatusCode() == 201) {
            System.out.println("Swagger file imported successfully.");
        } else {
            System.err.println("Failed to import Swagger file: " + importResponse.getStatusLine().getReasonPhrase());
        }

    }

    private static HttpResponse importSwaggerFile(HttpClient httpClient, String accessToken, File swaggerFile, String swaggerFileName) throws IOException {
        HttpPost importRequest = new HttpPost(IMPORT_API_ENDPOINT);
        importRequest.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addBinaryBody("file", swaggerFile, ContentType.APPLICATION_JSON, swaggerFileName);
        builder.addTextBody("overwrite", "true");
        builder.addTextBody("isDefaultVersion", "true");
        builder.addTextBody("swaggerUrl", "MY_SWAGGER_URL");
        importRequest.setEntity(builder.build());
        return httpClient.execute(importRequest);
    }
}

