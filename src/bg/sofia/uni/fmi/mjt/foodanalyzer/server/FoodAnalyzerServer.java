package bg.sofia.uni.fmi.mjt.foodanalyzer.server;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.commands.Command;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.controller.FDCClient;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.TooFewArgumentsException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.TooManyArgumentsException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.UnrecognizedCommandException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.WrongArgumentsException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.service.CacheInMemoryStorage;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

public class FoodAnalyzerServer {
    private static final int BUFFER_SIZE = 1024;
    private static final String HOST = "localhost";
    private final CacheInMemoryStorage cacheInMemoryStorage;
    private final FDCClient fdcClient;
    private final int port;
    private boolean isServerWorking;

    private ByteBuffer buffer;
    private Selector selector;

    public FoodAnalyzerServer(int port, CacheInMemoryStorage cacheInMemoryStorage, FDCClient fdcClient) {
        this.port = port;
        this.cacheInMemoryStorage = cacheInMemoryStorage;
        this.fdcClient = fdcClient;
    }

    public void start() {
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            selector = Selector.open();
            configureServerSocketChannel(serverSocketChannel, selector);
            this.buffer = ByteBuffer.allocate(BUFFER_SIZE);
            isServerWorking = true;
            while (isServerWorking) {
                try {
                    int readyChannels = selector.select();
                    if (readyChannels == 0) {
                        continue;
                    }

                    Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                    while (keyIterator.hasNext()) {
                        SelectionKey key = keyIterator.next();
                        if (key.isReadable()) {
                            SocketChannel clientChannel = (SocketChannel) key.channel();
                            String clientInput = getClientInput(clientChannel);
                            System.out.println(clientInput);
                            if (clientInput == null) {
                                continue;
                            }

                            try {
                                Command command = Command.of(clientInput);
                                String output = command.execute(cacheInMemoryStorage, fdcClient);

                                writeClientOutput(clientChannel, output);
                            } catch (UnrecognizedCommandException | TooFewArgumentsException |
                                     TooManyArgumentsException | WrongArgumentsException e) {
                                writeClientOutput(clientChannel, e.getMessage());
                            }


                        } else if (key.isAcceptable()) {
                            accept(selector, key);
                        }

                        keyIterator.remove();
                    }
                } catch (IOException e) {
                    System.out.println("Error occurred while processing client request: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException("failed to start server", e);
        }
    }

    public void stop() {
        this.isServerWorking = false;
        if (selector.isOpen()) {
            selector.wakeup();
        }
    }

    private void configureServerSocketChannel(ServerSocketChannel channel, Selector selector) throws IOException {
        channel.bind(new InetSocketAddress(HOST, this.port));
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_ACCEPT);
    }

    private String getClientInput(SocketChannel clientChannel) throws IOException {
        buffer.clear();

        int readBytes = clientChannel.read(buffer);
        if (readBytes < 0) {
            clientChannel.close();
            return null;
        }

        buffer.flip();

        byte[] clientInputBytes = new byte[buffer.remaining()];
        buffer.get(clientInputBytes);

        return new String(clientInputBytes, StandardCharsets.UTF_8);
    }

    private void writeClientOutput(SocketChannel clientChannel, String output) throws IOException {
        buffer.clear();
        buffer.put(output.getBytes());
        buffer.flip();

        clientChannel.write(buffer);
    }

    private void accept(Selector selector, SelectionKey key) throws IOException {
        ServerSocketChannel sockChannel = (ServerSocketChannel) key.channel();
        SocketChannel accept = sockChannel.accept();

        accept.configureBlocking(false);
        accept.register(selector, SelectionKey.OP_READ);
    }
}
