import List from "./List";
import React, {useState, useEffect, useRef} from 'react';
import TextBox from "./TextBox";
import SubmitButton from "./SubmitButton";
import Button from 'react-bootstrap/Button';

let list = [];

function Fridge() {

    // useState variable for name
    const [name, setName] = useState("User");

    // useState variable for text box input
    const [input, setInput] = useState("");

    const [ingredients, setIngredients] = useState([""]);


    // style details for root page
    const rootStyle = {
        backgroundColor: "white",
        height: '100vh'
    }

    // function for submit button
    const onSubmit = () => {
        if(!list.includes(input)) {
            //put current input into list
            list.unshift(input);

            //update ingredients
            setIngredients(list);

            //clear from this scope and from input box
            document.getElementById("inputBox").value = "";
            setInput("");
        }
    }

    const onChange = () => {

    }


    return (
        <div style={rootStyle} className="Fridge">
            {/*dynamic header*/}
            <h1 style={{top: 25}}>{name}'s Fridge</h1>
            {/*two buttons on side of page*/}
            <Button variant="success" size= "lg" style={{position: "absolute", left: 50, top: 25}}>Search for Recipes</Button>
            <Button variant="danger" size= "lg" style={{position: "absolute", right: 50, top: 25}}>Logout</Button>
            {/*two panes for lists and input*/}
            <List x={200} width={250} label={"Current Ingredients"} ingredients={ingredients} setter={setIngredients}/>
            <div>
            <List x={600} width={800} label={"Add an Ingredient"} ingredients={[]}/>
            {/*textbox for input*/}
            <div style={{position: "absolute", top: 400, right: 375}}>
                <TextBox input={setInput} change={onChange} label={"Name of Ingredient"}/>
                    <div style={{position: "relative", top: 50}}>
                        {/*submission button*/}
                        <SubmitButton label={"Submit"} onClick={onSubmit}/>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default Fridge;