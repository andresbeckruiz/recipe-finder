import List from "./List";
import React, {useState, useEffect, useRef} from 'react';
import SubmitButton from "./SubmitButton";
import Button from 'react-bootstrap/Button';
import SimilarRecipe from "./SimilarRecipe";

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
        <div style={rootStyle} className="Recipe">
            {/*dynamic header*/}
            <h1 style={{top: 25, color: "#000"}}>{name}</h1>
            {/*two buttons on side of page*/}
            <Button variant="success" size= "lg" style={{position: "absolute", left: 50, top: 25}}>Back to Fridge</Button>
            <Button variant="primary" size= "lg" style={{position: "absolute", right: 50, top: 25}}>Cook!</Button>

            {/*three similar recipes as suggestions*/}
            <div className={"flex-container"}>
                <SimilarRecipe label={"Recipe 1"}/>
                <SimilarRecipe label={"Recipe 2"}/>
                <SimilarRecipe label={"Recipe 3"}/>
            </div>
        </div>
    );
}

export default Recipe;