package manager;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;

public class Server {
    private DatagramChannel datagramChannel;
    private InetSocketAddress inetSocketAddress;

    public Server() throws IOException {
        datagramChannel = DatagramChannel.open();
        datagramChannel.configureBlocking(false);
        inetSocketAddress = new InetSocketAddress("localhost", 33011);
    }

    public String sendEcho(Request request) throws IOException, ClassNotFoundException {
        String first_line_from_client = request.getMessage();
        // отправка пакета
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(request);
        objectOutputStream.close();
        ByteBuffer byteBuffer = ByteBuffer.wrap(byteArrayOutputStream.toByteArray());
        datagramChannel.send(byteBuffer, inetSocketAddress);

        System.out.println("Отправлено сообщение на сервер: " + first_line_from_client); // Отладочное сообщение

        // прием пакета
        Selector selector = Selector.open();
        datagramChannel.register(selector, SelectionKey.OP_READ);
        selector.select(5000);  // Ожидаем готовности канала к чтению, таймаут 5 секунд
        Set<SelectionKey> selectedKeys = selector.selectedKeys();
        Iterator<SelectionKey> keyIterator = selectedKeys.iterator();
        ByteBuffer byteBuffer1 = ByteBuffer.allocate(3000);

        boolean responseReceived = false;

        while (keyIterator.hasNext()) {
            SelectionKey key = keyIterator.next();
            if (key.isReadable()) {
                byteBuffer1.clear();
                datagramChannel.receive(byteBuffer1);
                byteBuffer1.flip();
                if (byteBuffer1.hasRemaining()) {
                    ByteArrayInputStream bais = new ByteArrayInputStream(byteBuffer1.array(), byteBuffer1.position(), byteBuffer1.limit() - byteBuffer1.position());
                    ObjectInputStream ois = new ObjectInputStream(bais);
                    try {
                        request = (Request) ois.readObject();// Метод для обработки полученного запроса
                        ois.close();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
            keyIterator.remove();
        }

        if (!responseReceived || request.getMessage() == null || request.getMessage().equals(first_line_from_client)) {
            return "server are not available";
        }

        System.out.println("Получено сообщение от сервера: " + request.getMessage()); // Отладочное сообщение
        return request.getMessage();
    }

    public void close() throws IOException {
        datagramChannel.close();
    }
}
