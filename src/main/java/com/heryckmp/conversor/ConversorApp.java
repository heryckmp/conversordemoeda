package com.heryckmp.conversor;

import com.heryckmp.conversor.model.ExchangeRatesResponse;
import com.heryckmp.conversor.service.ExchangeRateApiService;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ConversorApp {

    // Remover a chave hardcoded
    // private static final String API_KEY = "SUA_CHAVE_AQUI"; // REMOVIDO
    private static ExchangeRateApiService apiService; // Inicializar depois
    private static Map<String, Double> taxasCache = null; // Cache simples para taxas
    private static String baseMoedaCache = "BRL"; // Usar BRL como base padrão

    // Lista das moedas de destino para conversão a partir/para a base (BRL)
    private static final List<String> MOEDAS_ALVO = List.of("USD", "EUR", "GBP", "ARS", "CLP");

    // Estrutura auxiliar para armazenar os detalhes de uma opção de conversão
    private static class OpcaoConversao {
        final String moedaOrigem;
        final String moedaDestino;

        OpcaoConversao(String origem, String destino) {
            this.moedaOrigem = origem;
            this.moedaDestino = destino;
        }
    }

    // Lista dinâmica das opções de conversão disponíveis no menu
    private static final List<OpcaoConversao> OPCOES_CONVERSAO = gerarOpcoesConversao();

    // Gera a lista de opções dinamicamente
    private static List<OpcaoConversao> gerarOpcoesConversao() {
        List<OpcaoConversao> opcoes = new ArrayList<>();
        for (String moedaAlvo : MOEDAS_ALVO) {
            opcoes.add(new OpcaoConversao(baseMoedaCache, moedaAlvo)); // Ex: BRL -> USD
            opcoes.add(new OpcaoConversao(moedaAlvo, baseMoedaCache)); // Ex: USD -> BRL
        }
        return List.copyOf(opcoes); // Retorna uma lista imutável
    }

    // Método para carregar a chave da API
    private static String carregarApiKey() {
        Properties prop = new Properties();
        String apiKey = null;
        try (InputStream input = new FileInputStream("config.properties")) {
            prop.load(input);
            apiKey = prop.getProperty("API_KEY");
            if (apiKey == null || apiKey.trim().isEmpty() || "SUA_CHAVE_API_AQUI".equals(apiKey.trim())) {
                 System.err.println("AVISO: API Key não encontrada ou não configurada em config.properties.");
                 System.err.println("Por favor, edite o arquivo config.properties com sua chave da ExchangeRate-API.");
                 return null; // Retorna null se a chave não for válida
            }
            apiKey = apiKey.trim(); // Garante que a chave retornada não tem espaços extras
        } catch (FileNotFoundException e) {
            System.err.println("Erro: Arquivo de configuração 'config.properties' não encontrado na raiz do projeto.");
        } catch (IOException ex) {
            System.err.println("Erro ao ler o arquivo de configuração: " + ex.getMessage());
        }
        return apiKey;
    }

    // Mapeamento de códigos de moeda para emojis (pode ser expandido)
    private static final Map<String, String> EMOJI_MOEDA = Map.of(
        "BRL", "🇧🇷",
        "USD", "🇺🇸",
        "EUR", "🇪🇺",
        "GBP", "🇬🇧",
        "ARS", "🇦🇷",
        "CLP", "🇨🇱"
    );

    // Método auxiliar para obter emoji da moeda
    private static String getEmojiForMoeda(String codigoMoeda) {
        return EMOJI_MOEDA.getOrDefault(codigoMoeda, "💰"); // Retorna emoji genérico se não encontrar
    }

    public static void main(String[] args) {
        // Carregar a chave da API primeiro
        String apiKey = carregarApiKey();
        if (apiKey == null) {
            System.out.println("Encerrando a aplicação devido a erro na configuração da API Key.");
            return; // Encerra se não conseguiu carregar a chave
        }
        // Inicializar o serviço com a chave carregada
        apiService = new ExchangeRateApiService(apiKey);

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
        String title = "✨ Bem-vindo ao Conversor de Moedas ✨";
        int width = 55; // Largura ajustada para o novo design
        String border = "═".repeat(width);
        String padding = " ".repeat((width - title.length()) / 2);

        System.out.println("╔" + border + "╗");
        System.out.println("║" + padding + title + padding + (title.length() % 2 != 0 ? " " : "") + "║"); // Ajuste para centralizar
        System.out.println("╠" + border + "╣");
        System.out.println("║ " + String.format("%-53s", "Escolha uma opção:") + " ║");
        System.out.println("║ " + String.format("%-53s", "") + " ║"); // Linha em branco
        System.out.println("║ 1️⃣ - Realizar Conversão de Moedas" + " ".repeat(19) + "║");
        System.out.println("║ " + String.format("%-53s", "") + " ║"); // Linha em branco
        System.out.println("║ 0️⃣ - Sair do Programa" + " ".repeat(30) + "║");
        System.out.println("╚" + border + "╝");
        System.out.print("▶️ Digite a opção desejada: ");
    }

    private static void iniciarConversorMoedas(Scanner scanner) {
        if (!atualizarTaxasDeCambio()) {
            System.out.println("Não foi possível obter as taxas de câmbio atualizadas. Tente novamente mais tarde.");
            return;
        }

        DecimalFormat df = new DecimalFormat("#,##0.00");
        int escolhaUsuario = -1;

        while (escolhaUsuario != 0) {
            exibirMenuMoedas(); // Exibe o menu gerado dinamicamente
            try {
                escolhaUsuario = scanner.nextInt();
                if (escolhaUsuario == 0) break;

                // Validar se a escolha está dentro do range das opções geradas
                if (escolhaUsuario < 1 || escolhaUsuario > OPCOES_CONVERSAO.size()) {
                    System.out.println("Opção inválida! Tente novamente.");
                    continue;
                }

                // Obter a opção de conversão escolhida (ajustar índice para base 0)
                OpcaoConversao selecionada = OPCOES_CONVERSAO.get(escolhaUsuario - 1);
                String moedaOrigem = selecionada.moedaOrigem;
                String moedaDestino = selecionada.moedaDestino;

                System.out.print("Digite o valor a ser convertido em " + moedaOrigem + ": ");
                double valorOriginal = scanner.nextDouble();

                // Obter a taxa necessária do cache
                // Se a origem é a base (BRL), usamos a taxa direta do destino.
                // Se o destino é a base (BRL), usamos o inverso da taxa da origem.
                Double taxa;
                if (moedaOrigem.equals(baseMoedaCache)) {
                    taxa = taxasCache.get(moedaDestino);
                } else { // moedaDestino é baseMoedaCache (BRL)
                    taxa = taxasCache.get(moedaOrigem);
                }

                // Verificar se a taxa necessária existe
                if (taxa == null) {
                     System.out.println("Erro: Taxa de câmbio para " + (moedaOrigem.equals(baseMoedaCache) ? moedaDestino : moedaOrigem) + " não encontrada.");
                    continue;
                }

                 // Calcular valor convertido
                double valorConvertido;
                if (moedaOrigem.equals(baseMoedaCache)) {
                    valorConvertido = valorOriginal * taxa; // Ex: BRL para USD (valor * taxa USD)
                } else { // moedaDestino é baseMoedaCache (BRL)
                     if (taxa == 0) {
                         System.out.println("Erro: Taxa de conversão para " + moedaOrigem + " é zero, impossível dividir.");
                         continue;
                     }
                    valorConvertido = valorOriginal / taxa; // Ex: USD para BRL (valor / taxa USD)
                }

                System.out.println("-------------------------------------------------");
                System.out.println("Resultado: " + df.format(valorOriginal) + " " + moedaOrigem + " = " + df.format(valorConvertido) + " " + moedaDestino);
                System.out.println("-------------------------------------------------");

            } catch (InputMismatchException e) {
                System.out.println("Erro: Entrada inválida. Por favor, insira um número.");
                scanner.next();
                escolhaUsuario = -1;
            } catch (Exception e) {
                System.out.println("Ocorreu um erro inesperado: " + e.getMessage());
                // e.printStackTrace(); // Descomentar para depuração
                scanner.next();
                escolhaUsuario = -1;
            }
            System.out.println();
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

    // Método modificado para exibir o menu dinamicamente com emojis e bordas
    private static void exibirMenuMoedas() {
        String title = "--- 💱 Conversor de Moedas (Base: " + baseMoedaCache + " " + getEmojiForMoeda(baseMoedaCache) + ") --- ";
        int width = 55; // Manter a mesma largura
        String border = "═".repeat(width);
        String padding = " ".repeat(Math.max(0,(width - title.length()) / 2));

        System.out.println("╔" + border + "╗");
        System.out.println("║" + padding + title + padding + (title.length() % 2 != 0 ? " " : "") + "║");
        System.out.println("╠" + border + "╣");

        for (int i = 0; i < OPCOES_CONVERSAO.size(); i++) {
            OpcaoConversao op = OPCOES_CONVERSAO.get(i);
            String origemEmoji = getEmojiForMoeda(op.moedaOrigem);
            String destinoEmoji = getEmojiForMoeda(op.moedaDestino);
            String optionText = String.format("%2d) %s %s ➡️ %s %s",
                                        (i + 1),
                                        origemEmoji,
                                        op.moedaOrigem,
                                        destinoEmoji,
                                        op.moedaDestino);
            // Adicionar padding direito para preencher a linha
            int remainingSpace = width - optionText.length() -1 ; // -1 para o espaço antes do ║
            String rightPadding = " ".repeat(Math.max(0, remainingSpace));
            System.out.println("║ " + optionText + rightPadding + "║");
        }

        System.out.println("╠" + border + "╣");
        String backOption = " 0) 🔙 Voltar ao Menu Principal";
        int backPadding = width - backOption.length() -1;
        System.out.println("║" + backOption + " ".repeat(Math.max(0, backPadding)) + "║");
        System.out.println("╚" + border + "╝");
        System.out.print("▶️ Digite a opção: ");
    }
} 