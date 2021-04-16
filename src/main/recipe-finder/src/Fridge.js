import List from "./List";
import React, {useState, useEffect} from 'react';
import axios from "axios";
import TextBox from "./TextBox";
import SubmitButton from "./SubmitButton";
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import {Link, useHistory} from 'react-router-dom'
import {Alert} from 'react-bootstrap'
import {useAuth} from "./contexts/AuthContext"
import Rating from "@material-ui/lab/Rating";
import ListItem from "./ListItem";
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

let ingredientRatings = {};

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
    const [ingredients, setIngredients] = useState(ingredientRatings);

    // useState variables for deletion modal
    const [modalIsOpen, setModalIsOpen] = useState(false);
    const [deleteIngredient, setDeleteIngredient] = useState(false);

    // useState variables for ingredient ratings modal
    const [ratingIsOpen, setRatingIsOpen] = useState(false);
    const [currentToRate, setCurrentToRate] = useState("");

    // useState hook for current ingredient to delete
    const [current, setCurrent] = useState("");

    //fixes a bug with displaying ratings
    const [list, setList] = useState([]);


    const [suggestions, setSuggestions] = useState([])
    const [autocorrectLoading, setAutocorrectLoading] = useState(true)
    // Axios Requests

    /*
     * Makes an axios request for adding ingredients
     */
    const addIngredient = (curr, event) => {

        console.log(curr);
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
                // nothing
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
                ingredientRatings[currentToRate] = rating;
                setIngredients(ingredientRatings);
                setList(Object.keys(ingredients))
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

    const checkValidIngredient = () => {
        let text = input.trim();
        //don't want to submit empty ingredient
        if (text == ""){
            toast.error("Cannot submit empty form, please enter an ingredient.")
            return
        }
        const toSend = {
            ingredient: text
        };

        let config = {
            headers: {
                "Content-Type": "application/json",
                'Access-Control-Allow-Origin': '*',
            }
        }

        axios.post(
            "http://localhost:4567/valid-ingredient",
            toSend,
            config
        )
            .then(response => {
                let valid = response.data["result"]
                //only want to submit anything if the ingredient isn't valid
                if (valid){
                    onSubmit(text)
                } else {
                    toast.error("Ingredient not found, please enter a valid ingredient.")
                }
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
        deleteIngredientRequest(current.trim());
    };

    // function for submit button
    const onSubmit = (text) => {
        //don't want to submit anything if the ingredient isn't valid

        //open modal if need be
        if (!ingredientRatings.hasOwnProperty(currentToRate)) {
            if (input !== "") {
                //clear from this scope and from input box
                document.getElementById("inputBox").value = "";
                setInput("");
            }

            //axios request
            addIngredient(text);
            setRatingIsOpen(true);
        }
    }

    //set global for listener

    const handleKeyDown = (event) => {
        if (event.key === 'Enter') {
            onSubmit(input);
        }
    }

    async function handleLogout() {
        setError("")

        try {
            await logout()
            history.push("/login")
        } catch {
            setError("Failed to log out")
        }
    }

    const getName = (email) => {

        const toSend = {
            name: email
        };

        let config = {
            headers: {
                "Content-Type": "application/json",
                'Access-Control-Allow-Origin': '*',
            }
        }

        axios.post(
            "http://localhost:4567/name",
            toSend,
            config
        )
            .then(response => {
                let name = response.data["name"]
                //update name variable
                setName(name)
            })
            .catch(function (error) {
                console.log(error);
            });
    }

    const getUserInventory = (email) => {

        const toSend = {
            name: email
        };

        let config = {
            headers: {
                "Content-Type": "application/json",
                'Access-Control-Allow-Origin': '*',
            }
        }

        axios.post(
            "http://localhost:4567/inventory",
            toSend,
            config
        )
            .then(response => {
                let inventory = response.data["inventory"]
                ingredientRatings = inventory;
                setIngredients(ingredientRatings);
            })

            .catch(function (error) {
                console.log(error);
            });
    }

    const createSuggestions = () => {
        setSuggestions([])
        setAutocorrectLoading(false)

        const postParameters = {
            text: input
        };

        fetch('http://localhost:4567/autocorrect', {
            method: 'post',
            body: JSON.stringify(postParameters),
            headers: {
                "Content-Type": "application/json",
                'Access-Control-Allow-Origin': '*',
            },
        })
            .then((response) => response.json())
            .then((data) => {
                let suggestionsTemp = []
                for (let word of data.results) {
                    console.log(word);
                    suggestionsTemp.push(word)
                }
                setSuggestions(suggestionsTemp)
                setAutocorrectLoading(true)
            })
    }

    //populates fridge with user inventory when page loads and gets user name
    useEffect(() => {
        ingredientRatings = {}
        getName(currentUser.email)
        getUserInventory(currentUser.email)
    },[]);


    return (
        <div style={rootStyle} className="Fridge" onKeyDown={handleKeyDown}>
            {/*dynamic header*/}
            <h1 style={{marginTop: 25}}>{name}'s Fridge</h1>
            {error && <Alert variant={"danger"}> {error} </Alert>}
            {/*two buttons on side of page*/}
            <Link to={"/RecipeSelection"}>
            <Button variant="success" size= "lg" style={{position: "absolute", left: 50, top: 25}}>Search for Recipes</Button>
            </Link>
            <Button onClick={handleLogout} variant="danger" size= "lg" style={{position: "absolute", right: 50, top: 25}}>Logout</Button>
            <Link to={"/profile"}>
            <Button variant="primary" size= "lg" style={{position: "absolute", right: 50, top: 80}}>Profile</Button>
            </Link>
            {/*two panes for lists and input*/}

            <List x={200} width={250} label={"Current Ingredients"} ingredients={ingredients} ingredientRater={rateIngredient} setter={setIngredients}
             setModalIsOpen={setModalIsOpen} deleteCurr={deleteIngredient} setDeleteCurr={setDeleteIngredient}
            setCurrent={setCurrent} list={list}/>

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
                        value={2.5}
                        precision={0.5}
                        size={"large"}
                        onChange={(event, newValue) => {
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
                {/*original top number was 225*/}
                <div style={{position: "relative", top: 100, left: 0, right:0}}>
                    <TextBox val={input} input={setInput} onKeyUp={createSuggestions}
                             label={"Name of Ingredient"} setCurr={setCurrentToRate}/>
                    <h4 hidden={autocorrectLoading} className={"text-dark"}> Loading...</h4>
                    <ul aria-live={"polite"}>
                        {/*using nested conditional rendering; don't want suggestions if nothing has
                        been typed, display text instead. if no suggestions, display text */}
                        {input.length !== 0 ?
                            [suggestions.length !== 0 || !autocorrectLoading ?
                            suggestions.map(item => {
                            return (
                                <ListItem item={item} setInput={setInput} input={input}
                                          setCurr={setCurrentToRate} curr={currentToRate} />
                            )
                        }) : <h5> No ingredients found </h5>]
                        : <h5>Start typing to see suggestions!</h5>}
                    </ul>
                    {/*original top number was 50*/}
                    <div id={"submit"} style={{position: "absolute", top: 225, left: 125}}>
                        {/*submission button*/}
                        <SubmitButton label={"Submit"} onClick={checkValidIngredient}/>
                        {/*this is for setting an error notification if ingredient is invalid*/}
                        <ToastContainer/>
                    </div>
                </div>
            </List>
            {/*textbox for input*/}
            </div>
        </div>
    );
}

export default Fridge;