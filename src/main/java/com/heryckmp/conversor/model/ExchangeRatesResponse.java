package com.heryckmp.conversor.model;

import java.util.Map;

import com.google.gson.annotations.SerializedName;

/**
 * Representa a estrutura da resposta da API ExchangeRate-API.
 */
public class ExchangeRatesResponse {

    private String result;

    @SerializedName("base_code") // Mapeia o nome do campo JSON para o nome da variável
    private String baseCode;

    @SerializedName("conversion_rates") // Mapeia o nome do campo JSON para o nome da variável
    private Map<String, Double> conversionRates;

    // Getters (necessários para Gson ou para acesso externo)
    public String getResult() {
        return result;
    }

    public String getBaseCode() {
        return baseCode;
    }

    public Map<String, Double> getConversionRates() {
        return conversionRates;
    }

    // Setters (geralmente não necessários para desserialização com Gson, mas podem ser úteis)
    public void setResult(String result) {
        this.result = result;
    }

    public void setBaseCode(String baseCode) {
        this.baseCode = baseCode;
    }

    public void setConversionRates(Map<String, Double> conversionRates) {
        this.conversionRates = conversionRates;
    }

    @Override
    public String toString() {
        return "ExchangeRatesResponse{" +
               "result='" + result + '\'' +
               ", baseCode='" + baseCode + '\'' +
               ", conversionRates=" + conversionRates +
               '}';
    }
} 