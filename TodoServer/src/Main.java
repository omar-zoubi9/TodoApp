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

public class Main {
    public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException {
        todoServer();
    }

    public static void todoServer() throws SQLException {
        try {
            ServerSocket serverSocket = new ServerSocket(8000);
            System.out.println("Server started ");

            while (true) {
                Socket socket = serverSocket.accept();
                QueryManager queryManager = new QueryManager();
                ObjectInputStream inputFromClient =
                        new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream outPutToClient =
                        new ObjectOutputStream(socket.getOutputStream());
                while(true){
                    int queryType = inputFromClient.readInt();
                    Object inputObject = inputFromClient.readObject();
                    switch (queryType) {
                        case 1 -> {
                            boolean signedUp = queryManager.signUp((UserDetails) inputObject);
                            outPutToClient.writeBoolean(signedUp);
                            System.out.println(signedUp);
                            outPutToClient.flush();
                        }
                        case 2 -> {
                            boolean signedIn = queryManager.signIn((UserDetails) inputObject);
                            outPutToClient.writeBoolean(signedIn);
                            outPutToClient.flush();
                        }
                        case 3 -> queryManager.addTodo((Todo)inputObject);
                        case 4 -> queryManager.deleteTodo((int) inputObject);
                        case 5 -> queryManager.editTodo((EditQuery) inputObject);
                        case 6 -> {
                            outPutToClient.writeObject(queryManager.getAllTodos());
                            outPutToClient.flush();
                        }
                        default -> throw new RuntimeException("query type is not valid");
                    }
                }
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

