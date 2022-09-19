package ClientManagement;

import QueryManaging.EditQuery;
import QueryManaging.QueryManager;
import Todo.Todo;
import UserManagement.UserDetails;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;

public class ClientHandler implements Runnable{
    private Socket client;
    private ObjectInputStream inputFromClient;
    private ObjectOutputStream outPutToClient;
    private QueryManager queryManager;

    public ClientHandler(Socket client) throws IOException, SQLException, ClassNotFoundException {
        this.client = client;
        try {
            inputFromClient = new ObjectInputStream(client.getInputStream());
            outPutToClient = new ObjectOutputStream(client.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            queryManager = new QueryManager();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        while(true){
            try{
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
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            } catch (IOException e) {
                System.out.println(e.getMessage());
            } catch (ClassNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
