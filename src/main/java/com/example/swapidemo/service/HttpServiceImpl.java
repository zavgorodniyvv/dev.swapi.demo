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
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class HttpServiceImpl implements HttpService {
    private static final Logger logger = LoggerFactory.getLogger(HttpServiceImpl.class);
    private final RetryTemplate retryTemplate;

    public HttpServiceImpl(RetryTemplate retryTemplate) {
        this.retryTemplate = retryTemplate;
    }

    @Override
    public String sendSingleGetRequest(String url) {
        HttpRequest request = createHttpRequest(url);
        HttpClient client = createHttpClient();
        logger.info("Sending request to SWAPI, url: {}", request.uri());

        HttpResponse<String> response;
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

    @Override
    public List<String> sendMultipleGetRequest(List<String> urls) {
        var client = createHttpClient();
        List<CompletableFuture<String>> future = urls.stream()
                .map(u -> client.sendAsync(createHttpRequest(u), HttpResponse.BodyHandlers.ofString())
                        .thenApply(HttpResponse::body))
                .toList();
        return future.stream()
                .map(CompletableFuture::join)
                .toList();
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

    private HttpClient createHttpClient() {
        return HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(20))
                .build();
    }

    private HttpRequest createHttpRequest(String url) {
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
