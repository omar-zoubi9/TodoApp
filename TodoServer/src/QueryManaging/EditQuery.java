package QueryManaging;

import java.io.Serializable;

public class EditQuery implements Serializable {
    private int editType;
    private int TodoId;
    private String newText;
    private boolean done;
    public void toggleDone(){
        done = !done;
    }
    public int getEditType() {
        return editType;
    }

    public void setEditType(int editType) {
        this.editType = editType;
    }

    public int getTodoId() {
        return TodoId;
    }

    public void setTodoId(int todoId) {
        TodoId = todoId;
    }

    public String getNewText() {
        return newText;
    }

    public void setNewText(String newText) {
        this.newText = newText;
    }
}
