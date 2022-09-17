package DatabaseManipulation;

import QueryManaging.EditQuery;
import Todo.Todo;
import UserManagement.UserDetails;

import java.sql.*;
import java.util.ArrayList;

public class DataBaseManipulator {
    private final Connection connection;
    private boolean userHasAccess = false;
    public void setCurrentUser(UserDetails currentUser) {
        this.currentUser = currentUser;
        userHasAccess = true;
    }
    UserDetails currentUser;
    public DataBaseManipulator() throws SQLException, ClassNotFoundException {
        connection = makeConnectionWithTodos();
    }
    private Connection makeConnectionWithTodos() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection
                ("jdbc:mysql://localhost/todos" ,
                        "omar", "1230");
    }
    public void signUp(UserDetails newUser) throws SQLException {
        Statement statement = connection.createStatement();
        String signUpQuery = buildSignUpQuery(newUser);
        statement.executeUpdate(signUpQuery);
    }
    public String buildSignInQuery(UserDetails user){
        return "SELECT * FROM user WHERE userName='" + user.getUserName() + "' AND userPass='" + user.getPassword() + "'";
    }
    public boolean signIn(UserDetails user) throws SQLException {
        Statement statement = connection.createStatement();
        String signInQuery = buildSignInQuery(user);
        ResultSet userSet = statement.executeQuery(signInQuery);
        if(userSet.next()){
            userHasAccess = true;
            setCurrentUser(user);
            return true;
        }else{
            System.out.println("user not found");
        }
        return false;
    }
    private String buildAddQuery(Todo todo){
        return "INSERT INTO todo" +
                " (title, body, done, lastModified, userName)" +
                " values ('" + todo.getTitle() + "', '" + todo.getBody() + "', " + todo.isDone() + ", '" + todo.getLastModified().toString() + "', '" +
                currentUser.getUserName() +
        "')";
    }
    public void addTodo(Todo newTodo) throws SQLException {
        Statement statement = connection.createStatement();
        String addQuery = buildAddQuery(newTodo);
        statement.executeUpdate(addQuery);
    }
    public String buildDeleteQuery(int id){
        return "DELETE FROM todo WHERE todoId=" + id + " AND userName='" + currentUser.getUserName() +"';";
    }
    public void deleteTodo(int todoToBeDeletedId) throws SQLException {
        Statement statement = connection.createStatement();
        String deleteQuery = buildDeleteQuery(todoToBeDeletedId);
        statement.executeUpdate(deleteQuery);
    }
    public String buildEditTitleQuery(EditQuery editQuery){
        return "UPDATE todo SET title='"+ editQuery.getNewText() + "' WHERE todoId=" + editQuery.getTodoId() + " AND userName='" + currentUser.getUserName() + "'";
    }
    public String buildEditBodyQuery(EditQuery editQuery){
        return "UPDATE todo SET body='"+ editQuery.getNewText() + "' WHERE todoId=" + editQuery.getTodoId() + " AND userName='" + currentUser.getUserName() + "'";
    }
    public String buildEditQuery(EditQuery editQuery){
        if(editQuery.getEditType() == 1)
            return buildEditTitleQuery(editQuery);
        else if(editQuery.getEditType() == 2)
            return buildEditBodyQuery(editQuery);
        else
            throw new RuntimeException("editQuery type is non-valid valid types:1 to edit title 2 to edit body");
    }
    private void toggleDone(EditQuery editQuery) throws SQLException {
        String getDoneFromTodo = "SELECT done FROM todo WHERE userName='" + currentUser.getUserName() + "' AND TodoId='" + editQuery.getTodoId() + "'" ;
        ResultSet doneSet = connection.createStatement().executeQuery(getDoneFromTodo);
        boolean done = false;
        if(doneSet.next()){
            done = doneSet.getBoolean("done");
        }else{
            System.out.println("wrong todoId");
            return;
        }

        String toggleQueryString = "UPDATE todo SET done="+ !done + " WHERE todoId=" + editQuery.getTodoId() +
                " AND userName='" + currentUser.getUserName() + "'";
        connection.createStatement().executeUpdate(toggleQueryString);
    }
    public void editTodo(EditQuery editQuery) throws SQLException {
        if(editQuery.getEditType() == 3){
            toggleDone(editQuery);
            return;
        }
        Statement statement = connection.createStatement();
        String editQueryString = buildEditQuery(editQuery);
        statement.execute(editQueryString);
    }
    private ArrayList<Todo> buildTodoArrayList(ResultSet todoSet) throws SQLException {
        ArrayList<Todo> todoList = new ArrayList<>();
        while (todoSet.next()) {
            int todoId = todoSet.getInt("todoId");
            String title = todoSet.getString("title");
            String body = todoSet.getString("body");
            boolean done = todoSet.getBoolean("done");
            Timestamp lastModified = todoSet.getTimestamp("lastModified");
            Todo extractedTodo = new Todo(todoId , title , body , done , lastModified);
            todoList.add(extractedTodo);
        }
    return todoList;
    }
    public ArrayList<Todo> getAllTodos() throws SQLException {
        if(!userHasAccess){
            throw new Error("you don't have access to the data base");
        }
        String getAllQuery = "SELECT * FROM todo Where userName='" + currentUser.getUserName() +"'";
        Statement statement = connection.createStatement();
        ResultSet todoSet = statement.executeQuery(getAllQuery);
        return buildTodoArrayList(todoSet);
    }
    public String buildSignUpQuery(UserDetails newUser){
        return "INSERT INTO user " +
                " (userName, userPass)" +
                " values ('"+ newUser.getUserName() + "', '" + newUser.getPassword() +
                "')";
    }
    public ArrayList<Integer> getIds() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet idSet = statement.executeQuery("SELECT * FROM todo WHERE userName='" + currentUser.getUserName() + "'");
        ArrayList<Integer> ids = new ArrayList<>();
        while(idSet.next()){
            ids.add(idSet.getInt("todoId"));
        }
        return ids;
    }
}
