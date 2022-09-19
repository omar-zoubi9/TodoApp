import ClientManagement.ClientHandler;
import QueryManaging.EditQuery;
import QueryManaging.QueryManager;
import Todo.Todo;
import UserManagement.UserDetails;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private static ExecutorService pool = Executors.newFixedThreadPool(5);
    public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException {
        todoServer();
    }
    public static void todoServer() throws SQLException {
        try {
            ServerSocket serverSocket = new ServerSocket(8000);
            System.out.println("Server started ");

            while (true) {
                System.out.println("server is waiting a connection");
                Socket socket = serverSocket.accept();
                System.out.println("server connected to a client");
                ClientHandler clientHandlerThread = new ClientHandler(socket);
                clientHandlers.add(clientHandlerThread);
                pool.execute(clientHandlerThread);
            }
        }
        catch(ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
    }
}

