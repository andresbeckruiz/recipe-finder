import './App.css';
import Fridge from "./Fridge";
import Signup from "./Signup";
import {Container} from 'react-bootstrap'
import {AuthProvider} from "./contexts/AuthContext";
import {BrowserRouter as Router, Switch, Route} from "react-router-dom";
import Dashboard from "./Dashboard";
import Login from "./Login";
import PrivateRoute from "./PrivateRoute";

function App() {
  return (
      <Container className={"d-flex align-items-center justify-content-center"} style={{minHeight: "100vh"}}>
          <div className={"w-100"} style={{maxWidth: '400px'}}>
              <Router>
                  <AuthProvider>
                      <Switch>
                          <PrivateRoute exact path={"/"} component={Dashboard}/>
                          <Route path={"/signup"} component={Signup}/>
                          <Route path={"/login"} component={Login}/>
                      </Switch>
                  </AuthProvider>
              </Router>
          </div>
      </Container>
    // <div className="App">
    //   {/*add components here for login landing page*/}
    //   {/*<Fridge/>*/}
    //   <h1>Hello world</h1>
    // </div>
  );
}

export default App;
