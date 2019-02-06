import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;
public class TaskTest {
    
  @Before //instructs JUNIt to run this code before each test
  public void setUp() { // the setup method creates a new instance of sql2o
    DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/to_do_test", "postgres", "dut");
  }

  @After  //deletes all our sample task objects after running each test since leftover objects can cause unexpected test failures
  public void tearDown() {
    try(Connection con = DB.sql2o.open()) { //try without resources-usefull when creating a two-way data stream thus closes the connection after the code is executed
      //the sql2o connection object is done by calling open() on the DV.sql2o
      String sql = "DELETE FROM tasks *;"; // deletes everything from task table
      con.createQuery(sql).executeUpdate(); //con. creates a connection by calling createquery on con and pass in the sql
   // the execute update automatically executes sql commands 
    }

  }

  @Test
  public void equals_returnsTrueIfDescriptionsAretheSame() {
    Task firstTask = new Task("Mow the lawn");
    Task secondTask = new Task("Mow the lawn");
    assertTrue(firstTask.equals(secondTask));
  }



@Test
public void save_returnsTrueIfDescriptionsAretheSame() {
  Task myTask = new Task("Mow the lawn");
  myTask.save(); //responsible for inserting this object into our database
  assertTrue(Task.all().get(0).equals(myTask)); //all retrieves Task objects
//.equals confirms the Task returned from our database is same as the Task we created localy
}

@Test
  public void all_returnsAllInstancesOfTask_true() {
    Task firstTask = new Task("Mow the lawn");
    firstTask.save();
    Task secondTask = new Task("Buy groceries");
    secondTask.save();
    assertEquals(true, Task.all().get(0).equals(firstTask));
    assertEquals(true, Task.all().get(1).equals(secondTask));
  }

@Test
public void save_assignsIdToObject() {
  Task myTask = new Task("Mow the lawn");
  myTask.save();
  Task savedTask = Task.all().get(0);
  assertEquals(myTask.getId(), savedTask.getId());
}

@Test
  public void getId_tasksInstantiateWithAnID() {
    Task myTask = new Task("Mow the lawn");
    myTask.save();
    assertTrue(myTask.getId() > 0);
  }

  @Test
  public void find_returnsTaskWithSameId_secondTask() {
    Task firstTask = new Task("Mow the lawn");
    firstTask.save();
    Task secondTask = new Task("Buy groceries");
    secondTask.save();
    assertEquals(Task.find(secondTask.getId()), secondTask);
  }

    @Test
    public void Task_instantiatesWithDescription_String() {
      Task myTask = new Task("Mow the lawn");
      assertEquals("Mow the lawn", myTask.getDescription());
    }

}