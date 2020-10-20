import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLOutput;

public class Server {
    // check input from client
    public static boolean validateData(String message) {
        String[] arrStr = message.split(",");
        if(arrStr.length != 2) return false;

        String[] userFirstInfo = arrStr[0].split("\"");
        String[] userSecondInfo = arrStr[1].split("\"");
        if(userFirstInfo[1].toUpperCase().equals("USER NAME")) {
            for(int i=0; i < userFirstInfo[3].length(); i++){
                int n = userFirstInfo[3].charAt(i);
                if (userFirstInfo[3].toUpperCase().charAt(i) < 65  || userFirstInfo[3].toUpperCase().charAt(i) > 90) return false;
            }
            if(userSecondInfo[1].toUpperCase().equals("USER AGE")) {
                for(int i=0; i < userSecondInfo[3].length(); i++){
                    if (userSecondInfo[3].toUpperCase().charAt(i) < 48  || (userSecondInfo[3].toUpperCase().charAt(i) > 57)) return false;
                }
            }
            else return false;
        }
        else if(userFirstInfo[1].toUpperCase().equals("USER AGE")) {
            for(int i=0; i < arrStr[1].length(); i++){
                if (userFirstInfo[3].toUpperCase().charAt(i) < 48  || userFirstInfo[3].toUpperCase().charAt(i) > 57) return false;
            }
            if(userSecondInfo[1].toUpperCase().equals("USER AGE")) {
                for(int i=0; i < userSecondInfo[3].length(); i++){
                    if (userSecondInfo[3].toUpperCase().charAt(i) < 65  || userSecondInfo[3].toUpperCase().charAt(i) > 90) return false;
                }
            }
            else return false;
        }

        return true;
    }

    public static void main(String args[]) {

        String HELLO_CLIENT = "200 Hello Client";
        String READY = "210 OK";
        String NOT_READY = "Type Hello Server to start";
        String USER_INF_OK = "211 User Infor OK";
        String USER_INF_ERR = "400 User Infor Error";
        String WRONG_SYNTAX = "Wrong syntax";

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
                else if(servReady) {
                    if(line.toUpperCase().equals("USER INFO")) {
                        os.write(READY);
                        typingUserInf = true;
                    }
                    else if(typingUserInf) {
                        if(validateData(line))  os.write(USER_INF_OK);
                        else os.write(USER_INF_ERR);
                        typingUserInf = false;
                    }
                    else os.write(WRONG_SYNTAX);

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

