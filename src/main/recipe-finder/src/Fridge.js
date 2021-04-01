import List from "./List";
import React, {useState, useEffect, useRef} from 'react';
import TextBox from "./TextBox";
import Button from "./Button";

function Fridge() {

    // useState variable for name
    const [name, setName] = useState("User");

    const rootStyle = {
        backgroundColor: "white",
        height: '100vh'
    }

    const onInput = () => {
        //put things
    }


    return (
        <div style={rootStyle} className="Fridge">
            {/*dynamic header*/}
            <h1>{name}'s Fridge</h1>
            {/*two panes for lists and input*/}
            <List x={200} width={250} label={"Current Ingredients"}/>
            <div>
            <List x={600} width={800} label={"Add an Ingredient"}/>
            {/*textbox for input*/}
            <div style={{position: "absolute", top: 400, right: 375}}>
                <TextBox change={onInput} label={"Name of Ingredient"}/>
                    <div style={{position: "relative", top: 50}}>
                        {/*submission button*/}
                        <Button label={"Submit"}/>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default Fridge;