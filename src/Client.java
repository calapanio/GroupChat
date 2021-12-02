import java.io.*;
import java.net.Socket;
import java.util.Scanner;

//caleb perry
//group chat
//12/1/2021
//purpose of project is to create a group chat that accepts multiple clients
//purpose of class is to act as a client and also handle sending output/getting input
public class Client {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String userName;
    public Client(Socket socket, String userName){
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.userName = userName;
        }catch (IOException e){
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }
    public void sendMessage(){
        try {
            bufferedWriter.write(userName);
            bufferedWriter.newLine();
            bufferedWriter.flush();
            //sends username to chat
            Scanner scanner = new Scanner(System.in);
            while (socket.isConnected()){
                String messageToSend = scanner.nextLine();
                bufferedWriter.write(userName + ": " + messageToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }//gets input from user with scanner and sends it out with bufferedWriter
        }catch (IOException e){
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }
    public void listenForMessage(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String messageFromGroupChat;
                while (socket.isConnected()){
                    try {
                        messageFromGroupChat = bufferedReader.readLine();
                        System.out.println(messageFromGroupChat);
                    }catch (IOException e){
                        closeEverything(socket, bufferedReader, bufferedWriter);
                    }
                }
            }//listens for messages and outputs them
        }).start();
    }
    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        try{
            if(bufferedReader != null) bufferedReader.close();
            if(bufferedWriter != null) bufferedWriter.close();
            if(socket != null) socket.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your username for the groupChat:");
        String userName = scanner.nextLine();//gets clients username
        Socket socket = new Socket("localhost", 54321);//NOTE: host is usually IP address
        Client client = new Client(socket, userName);
        client.listenForMessage();
        client.sendMessage();
    }
}
