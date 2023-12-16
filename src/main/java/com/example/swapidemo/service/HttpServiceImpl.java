package com.example.swapidemo.service;

import com.example.swapidemo.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Service
public class HttpServiceImpl implements HttpService {
    private static final Logger logger = LoggerFactory.getLogger(HttpServiceImpl.class);
    private final RetryTemplate retryTemplate;

    public HttpServiceImpl(RetryTemplate retryTemplate) {
        this.retryTemplate = retryTemplate;
    }

    @Override
    public String sendSingleGetRequest(String url) {
        HttpRequest request = getHttpRequest(url);
        HttpClient client = getHttpClient();
        logger.info("Sending request to SWAPI, url: {}", request.uri());

        HttpResponse<String> response = null;
        try {
            response = retryTemplate.execute(retryCallback -> sendSingleHttpRequest(client, request),
            recoveryCallback -> {
                throw new NotFoundException("Record not found");
            });
        } catch (Exception e) {
            String message = e.getMessage();
            logger.error(message);
            throw new NotFoundException(message);
        }

        return response.body();
    }


    private HttpResponse<String> sendSingleHttpRequest(HttpClient client, HttpRequest request) throws IOException, InterruptedException {
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if(response.statusCode() == 404){
            throw new NotFoundException("Record not found");
        }
        if(response.statusCode() != 200) {
            String message = "Response code: " + response.statusCode() + ", body: " + response.body();
            logger.warn("{}. Will try one more time.", message);
            throw new IOException("Response code: " + response.statusCode() + ", body: " + response.body());
        }
        return response;
    }

    private static HttpClient getHttpClient() {
        return HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(20))
                .build();
    }

    private static HttpRequest getHttpRequest(String url) {
        HttpRequest request;
        try {
            request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .GET()
                    .build();
        } catch (URISyntaxException e) {
            throw new NotFoundException(e.getMessage());
        }
        return request;
    }

}
