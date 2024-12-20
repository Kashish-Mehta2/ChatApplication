import java.io.*;
import java.net.*;

public class Client {
    BufferedReader br;
    PrintWriter out;
    Socket socket; // Declare socket as a class-level variable

    public Client() {
        try {
            System.out.println("Sending request to server");
            socket = new Socket("127.0.0.1", 777); // Now socket is accessible in both methods
            System.out.println("Connection Done");

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true); // auto-flush

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
                        System.out.println("Server Terminated the chat");
                        socket.close(); // Close socket when the server terminates
                        break;
                    }
                    System.out.println("Server : " + msg);
                }
            } catch (Exception e) {
                System.out.println("Connection is closed");
                // e.printStackTrace();
            }                System.out.println("Connection is closed");

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
                    out.println(content); // Send message to server

                    if (content.equals("exit")) {
                        socket.close(); // Close socket when the client types "exit"
                        break;
                    }
                }
            } catch (Exception e) {
                // e.printStackTrace();
                System.out.println("Connection is Closed");


            }
        };

        new Thread(r2).start();
    }

    public static void main(String[] args) {
        System.out.println("This is the client...");
        new Client();
    }
}
