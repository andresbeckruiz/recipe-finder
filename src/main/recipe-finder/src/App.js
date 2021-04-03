import './App.css';
import Fridge from "./Fridge";
import Signup from "./Signup";
import {Container} from 'react-bootstrap'

function App() {
  return (
      <Container className={"d-flex align-items-center justify-content-center"} style={{minHeight: "100vh"}}>
          <div className={"w-100"} style={{maxWidth: '400px'}}>
              <Signup/>
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
