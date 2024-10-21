package org.example;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.util.Scanner;

public class ClientApp {
    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Scanner scanner = new Scanner(System.in);

        try {
            // Запрос IP-адреса и порта
            System.out.print("Введите IP-адрес сервера: ");
            String serverIp = reader.readLine();
            System.out.print("Введите порт сервера: ");
            int serverPort = Integer.parseInt(reader.readLine());

            // Формирование URL для подключения клиента
            URL url = new URL("http://" + serverIp + ":" + serverPort + "/clientConnection");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            String clientInfo = "127.0.0.1:" + 59872; // или используйте реальный IP и порт
            try (OutputStream os = conn.getOutputStream()) {
                os.write(("clientInfo=" + clientInfo).getBytes());
                os.flush();
            }

            // Проверка ответа сервера
            int responseCode = conn.getResponseCode();
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
                System.out.println("POST request not worked. Response Code: " + responseCode);
            }

            // Цикл для отображения страниц
            while (true) {
                System.out.println("Выберите действие:");
                System.out.println("1 - Добавить перевод");
                System.out.println("2 - Перевести слово");
                System.out.println("3 - Обратный перевод");
                System.out.println("4 - История переводов");
                System.out.println("5 - Загрузить excel");
                System.out.println("0 - Выход");

                int choice = scanner.nextInt();
                // Изменяем логику - при выборе действия, просто печатаем URL
                switch (choice) {
                    case 1:
                        // Отправка запроса на добавление перевода
                        openBrowser(serverIp, serverPort, "/");
                        break;
                    case 2:
                        // Отправка запроса на перевод слова
                        openBrowser(serverIp, serverPort, "/translate");
                        break;
                    case 3:
                        // Отправка запроса на обратный перевод
                        openBrowser(serverIp, serverPort, "/reverseTranslate");
                        break;
                    case 4:
                        // Отправка запроса на просмотр истории
                        openBrowser(serverIp, serverPort, "/history");
                        break;
                    case 5: // Добавлено новое действие для загрузки excel
                        openBrowser(serverIp, serverPort, "/downloadExcel");
                        break;
                    case 0:
                        // Выход
                        System.out.println("Выход из приложения.");
                        return;
                    default:
                        System.out.println("Некорректный выбор. Попробуйте еще раз.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void openBrowser(String serverIp, int serverPort, String page) {
        String url = "http://" + serverIp + ":" + serverPort + page;
        try {
            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Не удалось открыть браузер.");
        }
    }
}