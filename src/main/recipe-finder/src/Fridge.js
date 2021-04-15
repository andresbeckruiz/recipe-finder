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
    const onSubmit = () => {
        let text = input.trim();
        if(input !== "") {
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

    const handleKeyDown = (event) => {
        if (event.key === 'Enter') {
            onSubmit();
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
                console.log("Inventory" + inventory)
                //update ratings
                for (var ingredient in inventory) {
                    ingredientRatings[ingredient] = inventory[ingredient]
                    console.log("INDIVIDUAL INGREDIENT" + ingredient)
                }
                setIngredients(ingredientRatings)
                console.log("Ingredient ratings:" + ingredientRatings)
            })

            .catch(function (error) {
                console.log(error);
            });
    }

    const createSuggestions = () => {
        console.log("YEP THIS IS BEING CALLED")
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
                // const listItems = document.getElementsByTagName("li")
                // for (let item of listItems) {
                //     console.log(item)
                //     item.addEventListener("click", (e) => {
                //         setInput(e.target.innerHTML)
                //     })
                // }
                setAutocorrectLoading(true)
            })
    }

    //populates fridge with user inventory when page loads and gets user name
    useEffect(() => {
        ingredientRatings = {}
        getName(currentUser.email)
        getUserInventory(currentUser.email)
        // autocorrectInput.addEventListener("keyup", () => {
        //     suggestionList.innerHTML = "";
        //     autocorrectLoading.style.display = "block";
        //
        //     const postParameters = {
        //         text: autocorrectInput.value
        //     };
        //
        //     fetch('/result', {
        //         method: 'post',
        //         body: JSON.stringify(postParameters),
        //         headers: {
        //             'Content-type': 'application/json; charset=UTF-8',
        //         },
        //     })
        //         .then((response) => response.json())
        //         .then((data) => {
        //             for (let word of data.results) {
        //                 console.log(word);
        //                 suggestionList.innerHTML += `<li tabIndex="0"> ${word} </li>`;
        //             }
        //             const listItems = document.getElementsByTagName("li")
        //             for (let item of listItems) {
        //                 console.log(item)
        //                 item.addEventListener("click", (e) => {
        //                     autocorrectInput.value = e.target.innerHTML
        //                 })
        //             }
        //             autocorrectLoading.style.display = "none";
        //         })
        // });
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
            <Button variant="primary" size= "sm" style={{position: "absolute", right: 50, top: 80}}>Profile</Button>
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
                        {suggestions.map(item => {
                            return (
                                <ListItem item={item} setInput={setInput} />
                            )
                        })}
                    </ul>
                    {/*original top number was 50*/}
                    <div id={"submit"} style={{position: "relative", top: 0, left: 150}}>
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