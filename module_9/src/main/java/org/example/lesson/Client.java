package org.example.lesson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Client {
    private static final int TIMEOUT_SECONDS = 30;

    public static void main(String[] args) {
        String serverAddress = "127.0.0.1";
        int port = 7182;


        try (Socket socket = new Socket(serverAddress, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in))
        ) {
            System.out.println("Подключаемся...");

            // Авторизация
            System.out.print(in.readLine() + " ");
            String login = consoleInput.readLine();
            out.println(login);

            System.out.print(in.readLine() + " ");
            String password = consoleInput.readLine();
            out.println(password);

            String authResponse = in.readLine();
            System.out.println("Сервер: " + authResponse);

            if (authResponse.contains("Ошибка авторизации")) {
                return;
            }

            // Таймер для автоматического отключения при отсутствии активности
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            Runnable timeoutTask = () -> {
                System.out.println("Нет активности. Закрываем соединение...");
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.exit(0);
            };

            // Запускаем таймер (если нет сообщений 30 секунд, соединение разрывается)
            scheduler.schedule(timeoutTask, TIMEOUT_SECONDS, TimeUnit.SECONDS);

            String clientMessage;
            String serverMessage;

            while (true) {
                System.out.print("Клиент: ");
                clientMessage = consoleInput.readLine();
                out.println(clientMessage);

                // Сбрасываем таймер
                scheduler.shutdownNow();
                scheduler = Executors.newScheduledThreadPool(1);
                scheduler.schedule(timeoutTask, TIMEOUT_SECONDS, TimeUnit.SECONDS);

                if (clientMessage.equalsIgnoreCase("exit")) {
                    System.out.println("Клиент отключился: " + socket.getInetAddress());
                    break;
                }

                serverMessage = in.readLine();
                // Сбрасываем таймер
                scheduler.shutdownNow();
                scheduler = Executors.newScheduledThreadPool(1);
                scheduler.schedule(timeoutTask, TIMEOUT_SECONDS, TimeUnit.SECONDS);

                if (Objects.isNull(serverMessage) || serverMessage.equalsIgnoreCase("exit")) {
                    System.out.println("Завершаем соединение...");
                    break;
                }

                System.out.println("Сервер: " + serverMessage);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
