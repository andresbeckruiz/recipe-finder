import List from "./List";
import React, {useState} from 'react';
import axios from "axios";
import TextBox from "./TextBox";
import SubmitButton from "./SubmitButton";
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import {Link, useHistory} from 'react-router-dom'
import {Alert} from 'react-bootstrap'
import {useAuth} from "./contexts/AuthContext"
import Rating from "@material-ui/lab/Rating";

let list = [];
let ingredientRatings = {};

// event listener for enter key
function submit() {}
document.addEventListener('keydown',function(e){
    if (e.key === "Enter") {
        submit();
    }
});

function Fridge() {

    // useState variable for name
    const [name, setName] = useState("User");

    //variables for user auth and logout
    const [error, setError] = useState("")
    const {currentUser, logout} = useAuth()
    const history = useHistory()

    // useState variable for text box input
    const [input, setInput] = useState("");

    // useState variable for ingredients list
    const [ingredients, setIngredients] = useState(list);

    // useState variables for deletion modal
    const [modalIsOpen, setModalIsOpen] = useState(false);
    const [deleteIngredient, setDeleteIngredient] = useState(false);

    // useState variables for ingredient ratings modal
    const [ratingIsOpen, setRatingIsOpen] = useState(false);
    const [currentToRate, setCurrentToRate] = useState("");

    // useState hook for current ingredient to delete
    const [current, setCurrent] = useState("");


    // Axios Requests

    /*
     * Makes an axios request for adding ingredients
     */
    const addIngredient = (curr, event) => {

        const toSend = {
            ingredient: curr
        };

        let config = {
            headers: {
                "Content-Type": "application/json",
                'Access-Control-Allow-Origin': '*',
            }
        }

        axios.post(
            "http://localhost:4567/enter-ingredient",
            toSend,
            config
        )
            .then(response => {
                //update ratings
                ingredientRatings[curr] = response.data["rating"];

            })

            .catch(function (error) {
                console.log(error);
            });
    }

    /*
    * Makes an axios request for ingredient rating
    */
    const rateIngredient = (curr, rating, event) => {

        const toSend = {
            ingredient: curr,
            rating: rating
        };

        let config = {
            headers: {
                "Content-Type": "application/json",
                'Access-Control-Allow-Origin': '*',
            }
        }

        axios.post(
            "http://localhost:4567/rate-ingredient",
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

    /*
 * Makes an axios request for ingredient rating
 */
    const deleteIngredientRequest = (curr, event) => {

        const toSend = {
            ingredient: curr
        };

        let config = {
            headers: {
                "Content-Type": "application/json",
                'Access-Control-Allow-Origin': '*',
            }
        }

        axios.post(
            "http://localhost:4567/delete-ingredient",
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

    // handlers for modals
    const handleClose = () => setModalIsOpen(false);
    const handleRatingClose = () => setRatingIsOpen(false);
    const handleCloseDelete = () => {
        setDeleteIngredient(true);
        setModalIsOpen(false);
        deleteIngredientRequest(input.trim());
    };

    // function for submit button
    const onSubmit = () => {
        console.log(ingredientRatings);
        let text = input.trim();
        if(!list.includes(text)) {
            //put current input into list
            list.unshift(text);

            //update ingredients
            setIngredients(list);

            //clear from this scope and from input box
            document.getElementById("inputBox").value = "";
            setInput("");
        }

        //open modal if need be
        if (!ingredientRatings.hasOwnProperty(currentToRate)) {
            setRatingIsOpen(true);
        }
        //axios request
        addIngredient(text);

    }

    //set global for listener
    submit = onSubmit;

    async function handleLogout() {
        setError("")

        try {
            await logout()
            history.push("/login")
        } catch {
            setError("Failed to log out")
        }
    }

    return (
        <div style={rootStyle} className="Fridge">
            {/*dynamic header*/}
            <h1 style={{marginTop: 25}}>{currentUser.email}'s Fridge</h1>
            {error && <Alert variant={"danger"}> {error} </Alert>}
            {/*two buttons on side of page*/}
            <Link to={"/RecipeSelection"}>
            <Button variant="success" size= "lg" style={{position: "absolute", left: 50, top: 25}}>Search for Recipes</Button>
            </Link>
            <Button onClick={handleLogout} variant="danger" size= "lg" style={{position: "absolute", right: 50, top: 25}}>Logout</Button>
            <Link to={"/update-password"}>
            <Button variant="primary" size= "sm" style={{position: "absolute", right: 50, top: 80}}>Update Password</Button>
            </Link>
            {/*two panes for lists and input*/}
            <List x={200} width={250} label={"Current Ingredients"} ingredients={ingredients} setter={setIngredients}
             setModalIsOpen={setModalIsOpen} deleteCurr={deleteIngredient} setDeleteCurr={setDeleteIngredient}
            setCurrent={setCurrent}/>

            {/*Modal for deletion*/}
            <Modal show={modalIsOpen} onHide={handleClose}>
                <Modal.Header closeButton>
                    <Modal.Title>Delete ingredient?</Modal.Title>
                </Modal.Header>
                <Modal.Body>"{current}" will be deleted permanently from your Fridge.</Modal.Body>
                <Modal.Footer>
                    <Button variant="light" onClick={handleClose}>
                        Cancel
                    </Button>
                    <Button variant="danger" onClick={handleCloseDelete}>
                        Delete
                    </Button>
                </Modal.Footer>
            </Modal>

            {/*Modal for ingredient ratings*/}
            <Modal show={ratingIsOpen} onHide={handleRatingClose}>
                <Modal.Header closeButton>
                    <Modal.Title>{currentToRate}</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <p style={{textAlign: "center"}}>How would you rate this ingredient?</p>
                    <div>
                    <Rating
                        style={{position: "relative", left: 150}}
                        name="simple-controlled"
                        value={ingredientRatings[input.trim()]}
                        precision={0.5}
                        size={"large"}
                        onChange={(event, newValue) => {
                            ingredientRatings[currentToRate] = newValue;
                            rateIngredient(currentToRate, newValue);
                            handleRatingClose();
                        }}
                    />
                    </div>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="light" onClick={handleRatingClose}>
                        Skip
                    </Button>
                </Modal.Footer>
            </Modal>

            <div>
            <List x={600} width={800} label={"Add an Ingredient"} ingredients={[]}>
                <div style={{position: "relative", top: 225, left: 0, right:0}}>
                    <TextBox input={setInput} label={"Name of Ingredient"} setCurr={setCurrentToRate}/>
                    <div id={"submit"} style={{position: "relative", top: 50, left: 150}}>
                        {/*submission button*/}
                        <SubmitButton label={"Submit"} onClick={onSubmit}/>
                    </div>
                </div>
            </List>
            {/*textbox for input*/}
            </div>
        </div>
    );
}

export default Fridge;