//caleb perry
//group chat
//12/1/2021
//purpose of project is to create a group chat that accepts multiple clients
//purpose of class is to act as a server and get clients and connect them
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;//listens for clients and creates socket to communicate
    public Server(ServerSocket serverSocket){
        this.serverSocket = serverSocket;
    }
    public void startServer(){
        try {
            while (!serverSocket.isClosed()){//checks for connections while server socket is open
                Socket socket = serverSocket.accept();//creates socket for when a new client is accepted
                System.out.println("a new client successfully connected");
                ClientHandler clientHandler = new ClientHandler(socket);
                Thread thread = new Thread(clientHandler);//creates new thread for clients
                thread.start();//runs thread
            }
        }catch (IOException e){}
    }
    public void closeServerSocket(){
        try {//if a serverSocket is not null and this method is called it will be closed
            if(serverSocket != null){
                serverSocket.close();
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException{
        ServerSocket serverSocket = new ServerSocket(54321);//creates serverSocket with port number 54321
        Server server = new Server(serverSocket);//creates new server
        server.startServer();//after server is created the server is started
    }
}
