import List from "./List";
import React, {useState, useEffect, useRef} from 'react';
import SubmitButton from "./SubmitButton";
import Button from 'react-bootstrap/Button';

function Recipe() {

    // useState variable for name
    const [name, setName] = useState("Recipe Name");

    // useState variable for text box input
    const [input, setInput] = useState("");

    const [ingredients, setIngredients] = useState([""]);


    // style details for root page
    const rootStyle = {
        backgroundColor: "white",
        height: '100vh'
    }


    return (
        <div style={rootStyle} className="Fridge">
            {/*dynamic header*/}
            <h1 style={{top: 25, color: "#000"}}>{name}</h1>
            {/*two buttons on side of page*/}
            <Button variant="success" size= "lg" style={{position: "absolute", left: 50, top: 25}}>Back to Fridge</Button>
            <Button variant="primary" size= "lg" style={{position: "absolute", right: 50, top: 25}}>Cook!</Button>

        </div>
    );
}

export default Recipe;