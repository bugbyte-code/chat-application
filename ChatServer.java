
import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {

    static ArrayList<ClientHandler> clients = new ArrayList<>();

    public static void main(String[] args) throws Exception {

        ServerSocket serverSocket = new ServerSocket(5000);
        System.out.println("Server started...");

        while (true) {
            Socket socket = serverSocket.accept();

            ClientHandler client = new ClientHandler(socket);
            clients.add(client);

            client.start();
        }
    }

    static class ClientHandler extends Thread {

        Socket socket;
        BufferedReader in;
        PrintWriter out;
        String name;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                name = in.readLine();
                broadcast("🔔 " + name + " joined the chat");

                String msg;

                while ((msg = in.readLine()) != null) {
                    String fullMsg = name + ": " + msg;
                    System.out.println(fullMsg);
                    broadcast(fullMsg);
                }

            } catch (Exception e) {
                System.out.println(name + " disconnected");
            }
        }

        void broadcast(String msg) {
            for (ClientHandler c : clients) {
                c.out.println(msg);
            }
        }
    }
}