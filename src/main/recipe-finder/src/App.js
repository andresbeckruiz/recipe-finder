import './App.css'
import Fridge from "./Fridge"
import Signup from "./Signup"
import {Container} from 'react-bootstrap'
import {AuthProvider} from "./contexts/AuthContext"
import {BrowserRouter as Router, Switch, Route} from "react-router-dom"
import Dashboard from "./Dashboard"
import Login from "./Login"
import PrivateRoute from "./PrivateRoute"
import UpdatePassword from "./UpdatePassword";
import Recipe from "./Recipe";
import ForgotPassword from "./ForgotPassword"
import RecipeSelection from "./RecipeSelection";
import Error from "./Error";

function App() {
  return (
      <Container className={"d-flex align-items-center justify-content-center"} style={{minHeight: "100vh"}}>
          <div className={"w-100"} style={{maxWidth: '400px'}}>
              <Router>
                  <AuthProvider>
                      <Switch>
                          <PrivateRoute exact path={"/"} component={Dashboard}/>
                          <PrivateRoute path={"/update-password"} component={UpdatePassword}/>
                          <PrivateRoute path={"/fridge"} component={Fridge}/>
                          <PrivateRoute path={"/recipe"} component={Recipe}/>
                          <PrivateRoute path={"/RecipeSelection"} component={RecipeSelection}/>
                          {/*is there a way to prevent a user typing in these routes once
                          logged in? or does this really matter*/}
                          <Route path={"/signup"} component={Signup}/>
                          <Route path={"/login"} component={Login}/>
                          <Route path={"/forgot-password"} component={ForgotPassword}/>
                          {/*if none of above routes, error page will display*/}
                          <Route component={Error}/>
                      </Switch>
                  </AuthProvider>
              </Router>
          </div>
      </Container>
  );
}

export default App;
