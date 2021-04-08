package edu.brown.cs.abeckruiggallantjfraust2jwebste5.App;
import static edu.brown.cs.abeckruiggallantjfraust2jwebste5.Data.Database.initialize;

import java.sql.SQLException;

public class RecipeApp {
  private User curUser = null;

  @SuppressWarnings("checkstyle:TodoComment")
  public RecipeApp() {
    //TODO: fix try catch!
    try {
      initialize("data/newdb.sqlite3");
    } catch (SQLException e) {
      System.out.println("ERROR");
    } catch (ClassNotFoundException e) {
      System.out.println("ERROR");
    }

    // Create Recipe Graph and Initialize
  }

  public User getCurUser() {
    return curUser;
  }

  public void setCurUser(User curUser) {
    this.curUser = curUser;
  }
}
