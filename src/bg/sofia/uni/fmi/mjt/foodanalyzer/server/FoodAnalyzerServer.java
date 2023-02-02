package bg.sofia.uni.fmi.mjt.foodanalyzer.server;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.controller.FDCClient;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.service.CacheStorage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.http.HttpClient;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FoodAnalyzerServer {
    private static final int SERVER_PORT = 4445;
    private static final int MAX_EXECUTOR_THREADS = 10;

    //TODO: Can make it as Interface and then DefaultCacheStorge as in the CocktailStorage lab
    private static CacheStorage cacheStorage = new CacheStorage();

    private static FDCClient fdcClient = new FDCClient(HttpClient.newBuilder().build());


    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(MAX_EXECUTOR_THREADS);
        Thread.currentThread().setName("Echo Server Thread");

        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {

            System.out.println("Server started and listening for connect requests");
            Socket clientSocket;

            while (true) {
                clientSocket = serverSocket.accept();

                System.out.println("Accepted connection request from client " + clientSocket.getInetAddress());
                ClientRequestHandler clientHandler =
                    new ClientRequestHandler(clientSocket, cacheStorage, fdcClient);
                executor.execute(clientHandler); // use a thread pool to launch a thread
            }

        } catch (IOException e) {
            throw new RuntimeException("There is a problem with the server socket", e);
        }
    }
}
