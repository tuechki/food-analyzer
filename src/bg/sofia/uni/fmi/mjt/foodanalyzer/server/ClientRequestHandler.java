package bg.sofia.uni.fmi.mjt.foodanalyzer.server;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.commands.Command;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.controller.FDCClient;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.UnrecognizedCommandException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.service.CacheStorage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientRequestHandler implements Runnable {

    private Socket socket;

    private CacheStorage cacheStorage;

    private FDCClient fdcClient;

    public ClientRequestHandler(Socket socket, CacheStorage cacheStorage, FDCClient fdcClient) {
        this.socket = socket;
        this.cacheStorage = cacheStorage;
        this.fdcClient = fdcClient;
    }

    @Override
    public void run() {

        Thread.currentThread().setName("Client Request Handler for " + socket.getRemoteSocketAddress());

        try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                Command command = Command.of(inputLine);
                if (command != null) {
                    String response = command.execute(cacheStorage, fdcClient);
                    out.write(response);
                } else {
                    out.write("Unknown command");
                }
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (UnrecognizedCommandException e) {
            //TODO: Return valuable information to the user.
            throw new RuntimeException(e);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
