package edu.brown.cs.abeckruiggallantjfraust2jwebste5.App;
import edu.brown.cs.abeckruiggallantjfraust2jwebste5.DatabaseHelpers.Database;

import static edu.brown.cs.abeckruiggallantjfraust2jwebste5.DatabaseHelpers.Database.initialize;
import static edu.brown.cs.abeckruiggallantjfraust2jwebste5.DatabaseHelpers.UserDatabase.initializeConn;

import java.sql.SQLException;

public class RecipeApp {
  private User curUser = null;

  public RecipeApp() {
    try {
      //initialize connection in database class
      initialize("data/newdb.sqlite3");
      //passes connection to user database class
      initializeConn(Database.getConn());
    } catch (SQLException ClassNotFoundException) {
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
