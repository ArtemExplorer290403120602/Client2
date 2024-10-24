package org.example;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Main {
    public static void main(String[] args) {
        try {
            // Получаем имя хоста
            InetAddress ip = InetAddress.getLocalHost();

            // Выводим IP-адрес
            System.out.println("IP-адрес вашего компьютера: " + ip.getHostAddress());
        } catch (UnknownHostException e) {
            System.out.println("Не удалось определить IP-адрес.");
            e.printStackTrace();
        }
    }
}