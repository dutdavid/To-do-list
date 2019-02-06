import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.sql2o.*;

public class Task {
  private String description;
  private boolean completed;
  private LocalDateTime createdAt;
  private int id;

  public static List<Task> all() { //returns all instances of  the task class
    String sql = "SELECT id, description FROM tasks"; //requests all id and description from the tasks table
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Task.class);
    }
  }
/*
  public void save() { //creating the save method
    try(Connection con = DB.sql2o.open()) { //establishing a connection
      String sql = "INSERT INTO tasks (description) VALUES (:description)"; //we construct an sql statement that uses the placeholder description--protects from sql injection
      con.createQuery(sql) //calling sql query passing in the sql statements
        .addParameter("description", this.description) //we replace the :description placeholder with this.description using .add
        .executeUpdate(); //we run the query
    }
  }  */

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO tasks(description) VALUES (:description)";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("description", this.description)
        .executeUpdate()
        .getKey();
    }
  }

  @Override
  public boolean equals(Object otherTask){
    if (!(otherTask instanceof Task)) {
      return false;
    } else {
      Task newTask = (Task) otherTask;
      return this.getDescription().equals(newTask.getDescription()) &&
             this.getId() == newTask.getId();
    }
  }
  
  public Task(String description) {
    this.description = description; //refers to the description property. required when a property name is exact same as a local variable
    //that is , because our argument is named description, we need to specify this.desc referring to task object's desc
    completed = false;
    createdAt = LocalDateTime.now();
  }
  
  public String getDescription() {
    return description;
  }

  public boolean isCompleted() {
    return completed;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public int getId() {
    return id;
  }

  public static Task find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM tasks where id=:id";
      Task task = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Task.class);
      return task;
    }
  }
}



  /*
    private String mDescription; //m is removed above coz all properties must match
    the names of the corresponding database columns
  and preceding database column names with an m is not the standard naming convention
    public Task(String description) {
      mDescription = description;
    }

    //we remove instances arraylist because our database will not be responsible for keeping track of tasks
  
    public String getDescription() {
      return mDescription;
    }
  }
  */