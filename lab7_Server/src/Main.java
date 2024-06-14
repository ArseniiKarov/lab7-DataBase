import manager.DBManager;
import manager.Server;

import java.net.SocketException;

public class Main {
    public static void main(String[] args) throws Exception {
        DBManager.ReadDB();
        try {
            Server server = new Server();
            server.listen();
        } catch (SocketException e) {
            System.out.println("SocketException: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}