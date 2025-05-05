package com.heryckmp.conversor;

import com.heryckmp.conversor.model.ExchangeRatesResponse;
import com.heryckmp.conversor.service.ExchangeRateApiService;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;
import java.text.DecimalFormat;

public class ConversorApp {

    // !! IMPORTANTE: Substitua pela sua API Key real !!
    private static final String API_KEY = "549106de557ec7239dd1bc2c";
    private static ExchangeRateApiService apiService = new ExchangeRateApiService(API_KEY);
    private static Map<String, Double> taxasCache = null; // Cache simples para taxas
    private static String baseMoedaCache = "BRL"; // Usar BRL como base padrão

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcao = -1;

        while (opcao != 0) {
            exibirMenuPrincipal();
            try {
                opcao = scanner.nextInt();

                switch (opcao) {
                    case 1:
                        iniciarConversorMoedas(scanner);
                        break;
                    // case 2: removido
                    case 0:
                        System.out.println("Saindo do programa...");
                        break;
                    default:
                        System.out.println("Opção inválida! Tente novamente.");
                        break;
                }
            } catch (InputMismatchException e) { // Ser mais específico aqui
                System.out.println("Erro: Entrada inválida. Por favor, insira um número (1 ou 0).");
                scanner.next(); // Limpa o buffer do scanner
                opcao = -1; // Reseta a opção para evitar loop infinito se a exceção persistir
            }
            System.out.println(); // Linha em branco para separar as interações
        }

        scanner.close();
        System.out.println("Programa finalizado.");
    }

    private static void exibirMenuPrincipal() {
        System.out.println("*************************************************");
        System.out.println("** Bem-vindo ao Conversor de Moedas! **"); // Ajustado
        System.out.println("Escolha uma opção:");
        System.out.println("1 - Realizar Conversão de Moedas"); // Ajustado
        //System.out.println("2 - Conversor de Temperatura (Opcional)"); // Removido
        System.out.println("0 - Sair");
        System.out.println("*************************************************");
        System.out.print("Digite a opção desejada: ");
    }

    private static void iniciarConversorMoedas(Scanner scanner) {
        // Tenta buscar taxas da API ou usar cache
        if (!atualizarTaxasDeCambio()) {
            // Se falhar, poderia usar taxas fixas como fallback ou apenas sair
            System.out.println("Não foi possível obter as taxas de câmbio atualizadas. Tente novamente mais tarde.");
            return; // Sai do método se não conseguir as taxas
        }

        DecimalFormat df = new DecimalFormat("#,##0.00"); // Formato monetário
        int opcaoConversao = -1;

        while (opcaoConversao != 0) {
            exibirMenuMoedas();
            try {
                opcaoConversao = scanner.nextInt();
                if (opcaoConversao == 0) break; // Sai do loop do conversor de moedas
                // Ajustar validação da opção para o novo range (1 a 10)
                if (opcaoConversao < 1 || opcaoConversao > 10) {
                    System.out.println("Opção inválida! Tente novamente.");
                    continue;
                }

                System.out.print("Digite o valor a ser convertido: ");
                double valorOriginal = scanner.nextDouble();
                double valorConvertido = 0;
                String moedaOrigem = "";
                String moedaDestino = "";

                // Obter taxas do cache
                Double taxaParaUsd = taxasCache.get("USD");
                Double taxaParaEur = taxasCache.get("EUR");
                Double taxaParaGbp = taxasCache.get("GBP");
                Double taxaParaArs = taxasCache.get("ARS");
                Double taxaParaClp = taxasCache.get("CLP");

                // Validação básica se as taxas existem no cache (a API pode não retornar todas)
                if (taxaParaUsd == null || taxaParaEur == null || taxaParaGbp == null || taxaParaArs == null || taxaParaClp == null) {
                    System.out.println("Erro: Taxas de câmbio necessárias não encontradas na resposta da API.");
                    continue;
                }

                switch (opcaoConversao) {
                    case 1: // BRL para USD
                        valorConvertido = valorOriginal * taxaParaUsd;
                        moedaOrigem = "BRL"; moedaDestino = "USD";
                        break;
                    case 2: // USD para BRL
                        valorConvertido = valorOriginal / taxaParaUsd;
                        moedaOrigem = "USD"; moedaDestino = "BRL";
                        break;
                    case 3: // BRL para EUR
                        valorConvertido = valorOriginal * taxaParaEur;
                        moedaOrigem = "BRL"; moedaDestino = "EUR";
                        break;
                    case 4: // EUR para BRL
                        valorConvertido = valorOriginal / taxaParaEur;
                        moedaOrigem = "EUR"; moedaDestino = "BRL";
                        break;
                    case 5: // BRL para GBP
                        valorConvertido = valorOriginal * taxaParaGbp;
                        moedaOrigem = "BRL"; moedaDestino = "GBP";
                        break;
                    case 6: // GBP para BRL
                        valorConvertido = valorOriginal / taxaParaGbp;
                        moedaOrigem = "GBP"; moedaDestino = "BRL";
                        break;
                    case 7: // BRL para ARS
                        valorConvertido = valorOriginal * taxaParaArs;
                        moedaOrigem = "BRL"; moedaDestino = "ARS";
                        break;
                    case 8: // ARS para BRL
                        valorConvertido = valorOriginal / taxaParaArs;
                        moedaOrigem = "ARS"; moedaDestino = "BRL";
                        break;
                    case 9: // BRL para CLP
                        valorConvertido = valorOriginal * taxaParaClp;
                        moedaOrigem = "BRL"; moedaDestino = "CLP";
                        break;
                    case 10: // CLP para BRL
                        valorConvertido = valorOriginal / taxaParaClp;
                        moedaOrigem = "CLP"; moedaDestino = "BRL";
                        break;
                    // Não precisa de default, já validado acima
                }

                System.out.println("-------------------------------------------------");
                // Usando o formato monetário
                System.out.println("Resultado: " + df.format(valorOriginal) + " " + moedaOrigem + " = " + df.format(valorConvertido) + " " + moedaDestino);
                System.out.println("-------------------------------------------------");

            } catch (InputMismatchException e) {
                System.out.println("Erro: Entrada inválida. Por favor, insira um número.");
                scanner.next(); // Limpa o buffer do scanner
                opcaoConversao = -1; // Reseta a opção
            } catch (Exception e) {
                System.out.println("Ocorreu um erro inesperado: " + e.getMessage());
                scanner.next(); // Limpa o buffer do scanner em caso de outros erros
                opcaoConversao = -1; // Reseta a opção
            }
            System.out.println(); // Linha em branco para separar
        }
        System.out.println("Retornando ao menu principal...");
    }

    /**
     * Tenta buscar as taxas de câmbio da API.
     * Armazena as taxas em cache se bem-sucedido.
     * @return true se as taxas foram obtidas com sucesso, false caso contrário.
     */
    private static boolean atualizarTaxasDeCambio() {
        // Se já temos taxas no cache, não busca novamente (pode adicionar lógica de expiração depois)
        if (taxasCache != null) {
            // System.out.println("Usando taxas de câmbio do cache.");
            return true;
        }

        System.out.println("Buscando taxas de câmbio atualizadas da API...");
        try {
            ExchangeRatesResponse response = apiService.getRates(baseMoedaCache);
            taxasCache = response.getConversionRates();
            System.out.println("Taxas obtidas com sucesso para a base " + response.getBaseCode() + "!");
            return true;
        } catch (IOException | InterruptedException e) {
            System.err.println("Erro de conexão ao buscar taxas: " + e.getMessage());
            // Tratar thread interrompida especificamente se necessário
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            return false;
        } catch (IllegalArgumentException e) {
             System.err.println("Erro de configuração: " + e.getMessage());
             return false;
        } catch (RuntimeException e) {
            System.err.println("Erro ao processar resposta da API: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Ocorreu um erro inesperado ao buscar taxas: " + e.getMessage());
            e.printStackTrace(); // Para depuração
            return false;
        }
    }

    private static void exibirMenuMoedas() {
        System.out.println("--- Conversor de Moedas ---");
        System.out.println("Escolha a conversão desejada:");
        System.out.println("1) Real Brasileiro (BRL) => Dólar Americano (USD)");
        System.out.println("2) Dólar Americano (USD) => Real Brasileiro (BRL)");
        System.out.println("3) Real Brasileiro (BRL) => Euro (EUR)");
        System.out.println("4) Euro (EUR) => Real Brasileiro (BRL)");
        System.out.println("5) Real Brasileiro (BRL) => Libra Esterlina (GBP)");
        System.out.println("6) Libra Esterlina (GBP) => Real Brasileiro (BRL)");
        System.out.println("7) Real Brasileiro (BRL) => Peso Argentino (ARS)");
        System.out.println("8) Peso Argentino (ARS) => Real Brasileiro (BRL)");
        System.out.println("9) Real Brasileiro (BRL) => Peso Chileno (CLP)");
        System.out.println("10) Peso Chileno (CLP) => Real Brasileiro (BRL)");
        System.out.println("0) Voltar ao Menu Principal");
        System.out.println("---------------------------");
        System.out.print("Digite a opção: ");
    }
} 