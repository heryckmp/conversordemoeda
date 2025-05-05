package com.heryckmp.conversor.service;

import com.google.gson.Gson;
import com.heryckmp.conversor.model.ExchangeRatesResponse;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Serviço para buscar taxas de câmbio da API ExchangeRate-API.
 */
public class ExchangeRateApiService {

    private final String apiKey;
    private final HttpClient httpClient;
    private final Gson gson;

    private static final String API_BASE_URL = "https://v6.exchangerate-api.com/v6/";

    public ExchangeRateApiService(String apiKey) {
        this.apiKey = apiKey;
        this.httpClient = HttpClient.newHttpClient();
        this.gson = new Gson();
    }

    /**
     * Busca as taxas de câmbio mais recentes para uma moeda base específica.
     *
     * @param baseCurrency O código da moeda base (ex: "BRL", "USD").
     * @return Um objeto ExchangeRatesResponse contendo as taxas.
     * @throws IOException Se ocorrer um erro de rede.
     * @throws InterruptedException Se a thread for interrompida durante a requisição.
     * @throws RuntimeException Se a API retornar um erro ou a resposta não for 200.
     */
    public ExchangeRatesResponse getRates(String baseCurrency) throws IOException, InterruptedException {
        if (apiKey == null || apiKey.trim().isEmpty()) {
            throw new IllegalArgumentException("API Key não pode ser nula ou vazia.");
        }
        if (baseCurrency == null || baseCurrency.trim().isEmpty()) {
            throw new IllegalArgumentException("Moeda base não pode ser nula ou vazia.");
        }

        String apiUrl = API_BASE_URL + apiKey + "/latest/" + baseCurrency.toUpperCase();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Falha ao buscar dados da API. Código de status: " + response.statusCode() + " Corpo: " + response.body());
        }

        ExchangeRatesResponse ratesResponse = gson.fromJson(response.body(), ExchangeRatesResponse.class);

        if (ratesResponse == null || !"success".equalsIgnoreCase(ratesResponse.getResult())) {
            throw new RuntimeException("Erro na resposta da API: " + response.body());
        }

        return ratesResponse;
    }
} 