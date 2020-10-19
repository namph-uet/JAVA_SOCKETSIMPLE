import java.io.*;
import java.net.*;
import java.util.Scanner;
 
public class Client {
   public static void main(String[] args) {
 
        // Địa chỉ máy chủ.
        final String serverHost = "localhost";
        final int cliPort = 9999; 
        Socket socketOfClient = null;
        BufferedWriter os = null;
        BufferedReader is = null;
 
       try {
           // request connect to server
           socketOfClient = new Socket(serverHost, cliPort);
 
           // create stream
           os = new BufferedWriter(new OutputStreamWriter(socketOfClient.getOutputStream()));
 
           is = new BufferedReader(new InputStreamReader(socketOfClient.getInputStream()));
 
        } catch (UnknownHostException e) {
           System.err.println("Don't know about host " + serverHost);
           return;
        } catch (IOException e) {
           System.err.println("Couldn't get I/O for the connection to " + serverHost);
           return;
        }

        System.out.println("Input a message to the echo server (QUIT for exit): ");
 
        try {
            // Ghi dữ liệu vào luồng đầu ra của Socket tại Client.
            String input;
            Scanner scanner = new Scanner(System.in);
            int input = scanner.nextLine();
            
            os.write(input);
            os.newLine();
            os.flush();
 
            // Read message from server
            String responseLine;
            while ((responseLine = is.readLine()) != null) {
                System.out.println("Server: " + responseLine);
                if (input.toUpperCase().equals("QUIT")) {
                    break;
                }
            }
 
            os.close();
            is.close();
            socketOfClient.close();
        } catch (UnknownHostException e) {
           System.err.println("Trying to connect to unknown host: " + e);
        }
         catch (IOException e) {
            System.err.println("IOException:  " + e);
        }
    }
}
 