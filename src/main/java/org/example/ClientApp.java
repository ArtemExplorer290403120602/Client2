package org.example;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class ClientApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.print("Введите порт сервера: ");
            int serverPort = Integer.parseInt(scanner.nextLine());
            String baseUrl = "http://localhost:" + serverPort;

            // Регистрация клиента на сервере
            sendClientInfo(baseUrl);

            // Цикл выбора действия
            while (true) {
                int choice = showMenuAndGetChoice(scanner);
                if (choice == 0) {
                    System.out.println("Выход из приложения.");
                    return;
                }
                navigateToPage(baseUrl, choice);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.out.println("Некорректный ввод порта. Пожалуйста, введите целое число.");
        }
    }

    private static void sendClientInfo(String baseUrl) throws IOException {
        URL url = new URL(baseUrl + "/clientConnection");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);

        String clientInfo = "192.168.32.1:59872"; // Замените на ваш IP и порт

        try (OutputStream os = conn.getOutputStream()) {
            os.write(("clientInfo=" + clientInfo).getBytes());
            os.flush();
        }

        int responseCode = conn.getResponseCode();
        System.out.println("Response Code: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            System.out.println("Response from server: " + response.toString());
        } else {
            System.out.println("POST request failed. Response Code: " + responseCode);
        }
    }

    private static int showMenuAndGetChoice(Scanner scanner) {
        System.out.println("Выберите действие:");
        System.out.println("1 - Добавить перевод");
        System.out.println("2 - Перевести слово");
        System.out.println("3 - Обратный перевод");
        System.out.println("4 - История переводов");
        System.out.println("5 - Загрузить excel");
        System.out.println("6 - Загрузить медиафайлы");
        System.out.println("7 - Показать медиа по ID");
        System.out.println("0 - Выход");

        return scanner.nextInt();
    }

    private static void navigateToPage(String baseUrl, int choice) {
        String page;
        switch (choice) {
            case 1: page = "/"; break;
            case 2: page = "/translate"; break;
            case 3: page = "/reverseTranslate"; break;
            case 4: page = "/history"; break;
            case 5: page = "/downloadExcel"; break;
            case 6: page = "/uploadMedia"; break;
            case 7: page = "/inputId"; break;
            default:
                System.out.println("Некорректный выбор. Попробуйте еще раз.");
                return;
        }
        openBrowser(baseUrl + page);
    }

    private static void openBrowser(String url) {
        try {
            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Не удалось открыть браузер.");
        }
    }
}