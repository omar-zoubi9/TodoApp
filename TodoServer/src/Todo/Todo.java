package Todo;

import java.io.Serializable;

public class Todo implements Serializable {
    private int id;
    private String title;
    private String Body;
    private java.sql.Timestamp lastModified;
    public Todo(){}
    public Todo(int id, String title, String body,boolean isDone , java.sql.Timestamp lastModified) {
        this.id = id;
        this.title = title;
        Body = body;
        this.lastModified = lastModified;
        this.isDone = isDone;
    }
    private boolean isDone;
    public String getTitle() {
        return title;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBody() {
        return Body;
    }

    public java.sql.Timestamp getLastModified() {
        return lastModified;
    }
    public boolean isDone() {
        return isDone;
    }
    public void setTitle(String title) {
        this.title = title;
        lastModified = new java.sql.Timestamp(new java.util.Date().getTime());
    }
    public void setBody(String body) {
        Body = body;
        lastModified = new java.sql.Timestamp(new java.util.Date().getTime());
    }
    public void setDoneToTrue(boolean done) {
        isDone = true;
    }
    public void setDoneToFalse(boolean done) {
        isDone = false;
    }
    @Override
    public String toString() {
        return "Todo{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", Body='" + Body + '\'' +
                ", lastModified=" + lastModified +
                ", isDone=" + isDone +
                '}';
    }
    public int getId() {
        return id;
    }
}
