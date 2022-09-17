package QueryManaging;

import DatabaseManipulation.DataBaseManipulator;
import Todo.Todo;
import UserManagement.PassManager;
import UserManagement.UserDetails;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class QueryManager {
    private final DataBaseManipulator dataBaseManipulator = new DataBaseManipulator();
    private final PassManager passManager = new PassManager();
    public QueryManager() throws SQLException, ClassNotFoundException, IOException {
    }
    public boolean signUp(UserDetails newUser) throws IOException, ClassNotFoundException {
        if(!passManager.isPassStrong(newUser.getPassword()))
            throw new Error("there is a problem in sending password, password is weak");
        newUser.setPassword(passManager.makePassHash(newUser.getPassword()));
        try {
            dataBaseManipulator.signUp(newUser);
        }catch (Exception SQLException){
            System.out.println("error can't put user in data base");
            return false;
        }
        dataBaseManipulator.setCurrentUser(newUser);
        return true;
    }
    public boolean signIn(UserDetails user) throws IOException, ClassNotFoundException {
        user.setPassword(passManager.makePassHash(user.getPassword()));
        try {
            return dataBaseManipulator.signIn(user);
        }catch (Exception SQLException){
            System.out.println("error can't sign in");
            return false;
        }
    }
    public void addTodo(Todo newTodo) throws IOException, ClassNotFoundException {
        try {
            dataBaseManipulator.addTodo(newTodo);
        }catch (Exception SQLException){
            System.out.println(SQLException.getMessage());
            System.out.println("error can't add todo");
        }
    }
    public void deleteTodo(int todoIdToDelete) throws IOException {
        try {
            dataBaseManipulator.deleteTodo(todoIdToDelete);
        }catch (Exception SQLException){
            System.out.println(SQLException.getMessage());
            System.out.println("error can't delete todo");
        }
    }
    public void editTodo(EditQuery editQuery) throws IOException, ClassNotFoundException {
        try {
            dataBaseManipulator.editTodo(editQuery);
        }catch (Exception SQLException){
            System.out.println(SQLException.getMessage());
            System.out.println("error can't edit todo");
        }
    }
    public ArrayList<Todo> getAllTodos() throws IOException, SQLException {
        return dataBaseManipulator.getAllTodos();
    }
}
