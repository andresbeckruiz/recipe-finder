import React, {useEffect, useState} from 'react';
import Button from 'react-bootstrap/Button';
import SimilarRecipe from "./SimilarRecipe";
import Rating from '@material-ui/lab/Rating';
import {Link} from "react-router-dom";
import axios from "axios";

function Recipe(props) {

    // useState variable for name
    const [name, setName] = useState("Recipe Name");

    const [value, setValue] = React.useState(2.5);

    const [ingredients, setIngredients] = useState("ingredients");

    const [preparation, setPreparation] = useState("preparation");


    // Axios Requests

    /*
     * Makes an axios request for finding similar recipes
     */
    const findSimilar = (name, event) => {

        const toSend = {
            recipe: name
        };

        let config = {
            headers: {
                "Content-Type": "application/json",
                'Access-Control-Allow-Origin': '*',
            }
        }

        axios.post(
            "http://localhost:4567/recipe",
            toSend,
            config
        )
            .then(response => {
                console.log(response.data)
                let object = response.data;

                //TODO: ADD THE REST PASSED IN

                //set up suggestions!
                for (var key in object) {
                    let recipe = object[key]
                    //set up recipe itself
                    if (key == 'recipe') {
                        setName(recipe["title"]);
                        setIngredients(recipe["ingredients"]);
                        setPreparation(recipe["instructions"]);
                    } else {  //store it dynamically to be accessed by SimilarRecipe objects
                        //get image and name
                        let name = recipe["recipeName"];
                        let img = recipe["src"];
                    }
                }

            })

            .catch(function (error) {
                console.log(error);
            });
    }

    /*
     * Makes an axios request for rating the recipe
     */
    const rateRecipe = (rating, event) => {

        const toSend = {
            rating: rating
        };

        let config = {
            headers: {
                "Content-Type": "application/json",
                'Access-Control-Allow-Origin': '*',
            }
        }

        axios.post(
            "http://localhost:4567/rate-recipe",
            toSend,
            config
        )
            .then(response => {
                //nothing
            })

            .catch(function (error) {
                console.log(error);
            });
    }


    // style details for root page
    const rootStyle = {
        backgroundColor: "white",
        height: '100vh'
    }

    //useEffect hook for initial render
    useEffect(() => {
        //set up recipe and similar recipes
        //setName(props.location.state.name);
         findSimilar(props.location.state.name);
        //get initial rating
    }, [])


    return (
        <div style={rootStyle} className="Recipe">
            {/*dynamic header*/}
            <h1 style={{top: 25, color: "#000"}}><b>{name}</b></h1>
            {/*two buttons on side of page*/}
            <Link to={"/RecipeSelection"}>
            <Button variant="success" size= "lg" style={{position: "absolute", left: 50, top: 25, width: 175}}>Back to Search </Button>
            </Link>
            <Link to={"/fridge"}>
                <Button variant="success" size= "lg" style={{position: "absolute", left: 50, top: 85, width: 175}}>Back to Fridge</Button>
            </Link>
            <Button variant="primary" size= "lg" style={{position: "absolute", right: 50, top: 25}}>Cook!</Button>

            <div style={{position: "relative", left: -500, marginTop: 150, marginLeft: 0}}>

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
            <div style={{position:"absolute", left: 0, right:0}} className={"flex-container"}>
                <SimilarRecipe label={"Recipe 1"}/>
                <SimilarRecipe label={"Recipe 2"}/>
                <SimilarRecipe label={"Recipe 3"}/>
            </div>
        </div>
    );
}

export default Recipe;