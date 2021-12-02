//caleb perry
//group chat
//12/1/2021
//purpose of project is to create a group chat that accepts multiple clients
//purpose of class is to send messages between clients and handle clients leaving/entering chat
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();//keeps track of all clients
    private Socket socket;
    private BufferedReader bufferedReader;//reads message from client
    private BufferedWriter bufferedWriter;//used to send messages from a client to a client
    private String clientUserName;
    public ClientHandler(Socket socket){
        try{
            this.socket = socket;//sets class socket to passed in socket
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            //transfers bytes to chars for sending
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //reads message from clients
            this.clientUserName = bufferedReader.readLine();//gets username from client
            clientHandlers.add(this);//adds client to group to receive and send messages
            broadCastMessage("SERVER: " + clientUserName + " has entered the chad:(");
        }catch (IOException e){
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }
    @Override
    public void run() {
        String messageFromClient;
        while (socket.isConnected()){
            try {
                messageFromClient = bufferedReader.readLine();//gets message from client
                broadCastMessage(messageFromClient);//displays message to all clients
            }catch (IOException e){
                closeEverything(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }
    public void broadCastMessage(String messageToSend){
        for(ClientHandler clientHandler : clientHandlers){
            try {
                if(!clientHandler.clientUserName.equals(clientUserName)){//sends message to everyone but sender
                    clientHandler.bufferedWriter.write(messageToSend);//gets message to send
                    clientHandler.bufferedWriter.newLine();//makes new line
                    clientHandler.bufferedWriter.flush();//sends out message
                }
            }catch (IOException e){
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }
    public void removeClientHandler(){
        clientHandlers.remove(this);
        broadCastMessage("SEVER: " + clientUserName + " has left the chat:)");
        //removes client and sends it to chat
    }
    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        removeClientHandler();
        try{
            if(bufferedReader != null) bufferedReader.close();
            if(bufferedWriter != null) bufferedWriter.close();
            if(socket != null) socket.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        //will close bufferedReader, Writer, and socket if not null to prevent errors
    }
}
