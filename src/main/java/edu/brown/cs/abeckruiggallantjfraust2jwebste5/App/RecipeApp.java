package edu.brown.cs.abeckruiggallantjfraust2jwebste5.App;
import edu.brown.cs.abeckruiggallantjfraust2jwebste5.Data.Database;

import static edu.brown.cs.abeckruiggallantjfraust2jwebste5.Data.Database.initialize;
import static edu.brown.cs.abeckruiggallantjfraust2jwebste5.Data.UserDatabase.initializeConn;

import java.sql.SQLException;

public class RecipeApp {
  private User curUser = null;

  @SuppressWarnings("checkstyle:TodoComment")
  public RecipeApp() {
    //TODO: fix try catch!
    try {
      initialize("data/newdb.sqlite3");
      initializeConn(Database.getConn());
    } catch (SQLException e) {
      System.out.println("ERROR");
    } catch (ClassNotFoundException e) {
      System.out.println("ERROR");
    }
  }

  public User getCurUser() {
    return curUser;
  }

  public void setCurUser(User curUser) {
    this.curUser = curUser;
  }
}
