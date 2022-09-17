package Factory;

import Todo.Todo;

import java.util.Scanner;

public class TodoFactory {
    public Todo makeTodoWithUserInput(){
        String input;
        Scanner scanner = new Scanner(System.in);
        Todo newTodo = new Todo();
        System.out.println("enter todo title: ");
        input = scanner.nextLine();
        newTodo.setTitle(input);
        System.out.println("enter todo body: ");
        input = scanner.nextLine();
        newTodo.setBody(input);
        return newTodo;
    }
}
