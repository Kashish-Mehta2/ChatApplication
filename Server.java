import java.io.*;
import java.net.*;

class Server {

    BufferedReader br;
    PrintWriter out;
    Socket socket; // Declare the socket at the class level to access it in both methods

    public Server() {
        try {
            ServerSocket server = new ServerSocket(777);
            System.out.println("Server is ready to accept connection");
            System.out.println("waiting ....");
            socket = server.accept(); // Now socket is accessible in both methods
            br = new BufferedReader(new InputStreamReader(socket.getInputStream())); // Correct initialization
            out = new PrintWriter(socket.getOutputStream(), true); // Correct initialization (auto-flush)

            startReading();
            startWriting();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startReading() {
        System.out.println("Reader started...");
        Runnable r1 = () -> {
            try {
                while (true) {
                    String msg = br.readLine();
                    if (msg.equals("exit")) {
                        System.out.println("Client Terminated the chat");
                        socket.close(); // Close socket when the client terminates
                        break;
                    }
                    System.out.println("Client: " + msg);
                 }
                System.out.println("Connection is closed");
            } catch (Exception e) {
                // e.printStackTrace();
                System.out.println("Connection is closed");

            }
        };
        new Thread(r1).start();
    }

    public void startWriting() {
        System.out.println("Writer started...");
        Runnable r2 = () -> {
            try {
                BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                while (!socket.isClosed()) {
                    String content = br1.readLine();
                    out.println(content); // Send message to client
                    if (content.equals("exit")) {
                        socket.close(); // Close socket when the server types "exit"
                        break;
                    }

                }
            } catch (Exception e) {
                // e.printStackTrace();
                System.out.println("Connection is closed");
            }
        };

        new Thread(r2).start();
    }

    public static void main(String[] args) {
        System.out.println("This is the server... starting the server");
        new Server();
    }
}
