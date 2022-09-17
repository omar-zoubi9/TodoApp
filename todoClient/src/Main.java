import Factory.TodoFactory;
import QueryManaging.EditQuery;
import Todo.Todo;
import UserManagement.UserDetails;
import inputManaging.InputManager;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        client();
    }
    private static void client() throws IOException, ClassNotFoundException {
        boolean signedIn = false;
        InputManager inputManager = new InputManager();
        Scanner scanner = new Scanner(System.in);
        UserDetails user;
        Socket socket = new Socket("127.0.0.1" , 8000);

        ObjectOutputStream outputToServer = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream inputFromServer = new ObjectInputStream(socket.getInputStream());
        while(!signedIn){
            System.out.println("please enter 0 to exit 1 to sign up, 2 to sign in: ");
            switch (scanner.nextInt()){
                case 0 -> {return;}
                case 1 -> {
                    user = inputManager.queryUserToInputNewDetails();
                    outputToServer.writeInt(1);
                    outputToServer.writeObject(user);
                    outputToServer.flush();
                }
                case 2 -> {
                    user = inputManager.queryUserToInputHisDetails();
                    outputToServer.writeInt(2);
                    outputToServer.writeObject(user);
                    outputToServer.flush();
                }
                default -> throw new RuntimeException("input is not valid");
            }
            signedIn = inputFromServer.readBoolean();//check if it's empty
            System.out.println(signedIn);
        }

        while(true){
            String input;
            int integerInput;
            System.out.println("please enter 0 to exit, 1 to add todo, 2 to delete todo, 3 to edit todo, 4 to see all todos: ");
            switch (scanner.nextInt()){
                case 0 -> {return;}
                case 1 -> {
                    TodoFactory todoFactory = new TodoFactory();
                    Todo newTodo = todoFactory.makeTodoWithUserInput();
                    outputToServer.writeInt(3);
                    outputToServer.writeObject(newTodo);
                    outputToServer.flush();
                }
                case 2 -> {
                    System.out.println("enter a todo id to delete or 0 to exit: ");
                    int intInput = scanner.nextInt();
                    if(intInput == 0)
                        return;
                    outputToServer.writeInt(4);
                    outputToServer.writeObject(intInput);
                    outputToServer.flush();
                }
                case 3 -> {
                    EditQuery editQuery = new EditQuery();
                    System.out.println("enter a todo id to edit or 0 to exit: ");
                    int intInput = scanner.nextInt();
                    scanner.nextLine();
                    editQuery.setTodoId(intInput);
                    if(intInput == 0)
                        return;
                    System.out.println("enter 1 to edit title, 2 to edit body, 3 to toggle mark as done: ");
                    integerInput = scanner.nextInt();
                    scanner.nextLine();
                    switch (integerInput) {
                        case 1 -> {
                            System.out.println("enter a new title: ");
                            input = scanner.nextLine();
                            editQuery.setNewText(input);
                        }
                        case 2 -> {
                            System.out.println("enter a new body: ");
                            input = scanner.nextLine();
                            editQuery.setNewText(input);
                        }
                        case 3 -> {
                            System.out.println("toggling");
                        }
                        default ->
                                throw new RuntimeException("not valid input");
                    }
                    editQuery.setEditType(integerInput);
                    outputToServer.writeInt(5);
                    outputToServer.writeObject(editQuery);
                    outputToServer.flush();
                }
                case 4 -> {
                    outputToServer.writeInt(6);
                    outputToServer.writeObject(null);
                    outputToServer.flush();
                    ArrayList<Todo> todoArrayList = (ArrayList<Todo>) inputFromServer.readObject();
                    for (int i = 0; i < todoArrayList.size() ; i++) {
                        System.out.println( todoArrayList.get(i));
                    }
                }
                default -> throw new RuntimeException("input is not valid");
            }
        }
    }
}