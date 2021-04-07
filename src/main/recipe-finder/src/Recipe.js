import React, {useState, useEffect, useRef} from 'react';
import Button from 'react-bootstrap/Button';
import SimilarRecipe from "./SimilarRecipe";
import Rating from '@material-ui/lab/Rating';
import {Link} from "react-router-dom";

function Recipe() {

    // useState variable for name
    const [name, setName] = useState("Recipe Name");

    const [value, setValue] = React.useState(2.5);

    const [ingredients, setIngredients] = useState("ingredients");

    const [preparation, setPreparation] = useState("preparation");


    // style details for root page
    const rootStyle = {
        backgroundColor: "white",
        height: '100vh'
    }


    return (
        <div style={rootStyle} className="Recipe">
            {/*dynamic header*/}
            <h1 style={{top: 25, color: "#000"}}><b>{name}</b></h1>
            {/*two buttons on side of page*/}
            <Link to={"/"}>
            <Button variant="success" size= "lg" style={{position: "absolute", left: 50, top: 25}}>Back to Fridge</Button>
            </Link>
            <Button variant="primary" size= "lg" style={{position: "absolute", right: 50, top: 25}}>Cook!</Button>

            <div style={{position: "relative", marginTop: 150, marginLeft: 100}}>

                {/*ratings in header*/}
                <Rating
                    style={{position: "relative", top: 40, left: 1250}}
                    name="simple-controlled"
                    value={value}
                    precision={0.5}
                    size={"large"}
                    onChange={(event, newValue) => {
                        setValue(newValue);
                    }}
                />

                {/*ingredients header*/}
                <h2 className={"recipe"}><u>Ingredients</u></h2>


                {/*paragraph for ingredients*/}
                <p className={"inside-paragraph"}>{ingredients}</p>

                {/*instructions header*/}
                <h2 className={"recipe"}><u>Preparation</u></h2>

                {/*paragraph for instruction*/}
                <p className={"inside-paragraph"}>{preparation}</p>

                {/*similar recipes header*/}
                <h2 className={"recipe"} style={{position: "relative", bottom: -100}}><u>Similar Recipes</u></h2>

            </div>
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