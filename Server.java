import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    // check input from client
    public static boolean validateData(String message) {
        String[] arrStr = message.split(",");

        if(arrStr.length == 2
                || arrStr.length == 0
                || arrStr.length == 3
                || arrStr.length > 4
        ) return false;

        if(arrStr[0].equals("User name")) {
            for(int i=0; i < arrStr[1].length(); i++){
                if (arrStr[1].charAt(i) < 65  || arrStr[1].charAt(i) > 90) return false;
            }
            if(arrStr[2].equals("User age")) {
                for(int i=0; i < arrStr[3].length(); i++){
                    if (arrStr[3].charAt(i) < 48  || (arrStr[3].charAt(i) > 57)) return false;
                }
            }
        }
        else if(arrStr[0].equals("User age")) {
            for(int i=0; i < arrStr[1].length(); i++){
                if (arrStr[1].charAt(i) < 48  || arrStr[1].charAt(i) > 57) return false;
            }
            for(int i=0; i < arrStr[3].length(); i++){
                if (arrStr[3].charAt(i) < 65  || arrStr[3].charAt(i) > 90) return false;
            }
        }

        return true;
    }

    public static void main(String args[]) {

        String HELLO_CLIENT = "200 Hello Client";
        String READY = "210 OK";
        String NOT_READY = "Type Hello Server to start";
        String USER_INF_OK = "211 User Infor OK";
        String USER_INF_ERR = "400 User Infor Error";

        final int serverPort = 9999;
        ServerSocket listener = null;
        String line;
        BufferedReader is;
        BufferedWriter os;
        Socket socketOfServer = null;
        boolean servReady = false;
        boolean typingUserInf = false;


        // create server listen in port 90000
        try {
            listener = new ServerSocket(serverPort);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(1);
        }

        try  {
            System.out.println("Server is waiting for client...");


            // Accept connect request from a client
            // get socket object

            socketOfServer = listener.accept();
            System.out.println("Accept a client!");


            // open stream data
            is = new BufferedReader(new InputStreamReader(socketOfServer.getInputStream()));
            os = new BufferedWriter(new OutputStreamWriter(socketOfServer.getOutputStream()));


            // Received a message from the client
            while (true) {
                // read message from client
                line = is.readLine();
                System.out.println("Received a connection from a client: " + line);

                if(line.toUpperCase().equals("HELLO SERVER")) {
                    servReady = true;
                    os.write(HELLO_CLIENT);
                }

                if(servReady) {
                    if(!typingUserInf && line.toUpperCase().equals("USER INFO")) {
                        os.write(READY);
                        typingUserInf = true;
                    }
                    else if(typingUserInf) {
                        if(validateData(line))  os.write(USER_INF_OK);
                        else os.write(USER_INF_ERR);
                    }
                }
                else {
                    os.write(NOT_READY);
                }

                // end line
                os.newLine();
                // send data to client
                os.flush();

                // Nếu người dùng gửi tới QUIT (Muốn kết thúc trò chuyện).
                if (line.toUpperCase().equals("QUIT")) {
                    break;
                }
            }

        } catch (IOException e) {
            System.out.println(e);
            e.printStackTrace();
        }
        System.out.println("Sever stopped!");
    }
}
