import List from "./List";
import React, {useState, useEffect} from 'react';
import axios from "axios";
import TextBox from "./TextBox";
import SubmitButton from "./SubmitButton";
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import {Link, useHistory} from 'react-router-dom'
import {Alert, Toast} from 'react-bootstrap'
import {useAuth} from "./contexts/AuthContext"
import Rating from "@material-ui/lab/Rating";
import ListItem from "./ListItem";
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
let current;

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
    const [ingredientRatings, setIngredientRatings] = useState({});

    // useState variables for deletion modal
    const [modalIsOpen, setModalIsOpen] = useState(false);
    const [deleteIngredient, setDeleteIngredient] = useState(false);

    // useState variables for ingredient ratings modal
    const [ratingIsOpen, setRatingIsOpen] = useState(false);
    const [currentToRate, setCurrentToRate] = useState("");

    // useState hook for current ingredient to delete
    const [current, setCurrent] = useState("");


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
                let ratings = ingredientRatings;
                ratings[currentToRate] = rating;
                if(ingredientRatings == ratings) {
                    console.log('sammmeee')
                }
                setIngredientRatings(ratings);
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
        console.log("Submitting happening, valid!")
        //don't want to submit anything if the ingredient isn't valid
        if (input !== "") {
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

    const style = {
        backgroundColor: "#2776ED",
        height: 600,
        width: 250,
        position: "absolute",
        top: 150,
        left: 200
    }

    const innerStyle = {
        height: 590,
        width: 250,
        position: "absolute",
        bottom: 10,
        overflow: "auto"
    }
    //
    // function deleteCurrent() {
    //     let curr = ingredientRatings;
    //     delete curr[this.current];
    //     setIngredientRatings(curr);
    //     setDeleteIngredient(false);
    // }
    //
    // useEffect(() => {
    //     if(deleteIngredient) {
    //         deleteCurrent();
    //     }
    // }, [deleteIngredient])

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
                console.log('getting inventory')
                //update ratings
                let ratings = ingredientRatings;
                for (var ingredient in inventory) {
                    ratings[ingredient] = inventory[ingredient]
                }
                setIngredientRatings(ratings)
                console.log("SET RATING TO: ")
                console.log(ratings)
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

    useEffect(() => {
      console.log("RATINGS: ")
      console.log(ingredientRatings)
    }, [ingredientRatings])

    //populates fridge with user inventory when page loads and gets user name
    useEffect(() => {
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
            <Button variant="primary" size= "sm" style={{position: "absolute", right: 50, top: 80}}>Profile</Button>
            </Link>
            {/*two panes for lists and input*/}


            <div>{Object.keys(ingredientRatings)}</div>
            {/*<div style={style} className="List">*/}
            {/*    <h4 style={{position: "absolute", top: -40}}>"Current Ingredients"</h4>*/}
            {/*    <div style={innerStyle} className="List">*/}
            {/*        <div style={{marginTop: 25}}>*/}
            {/*            {Array.from(Object.keys(ingredientRatings)).map(r =>*/}
            {/*                <div key={r} className="ingredient">*/}
            {/*                    <p style={{textAlign: "center", cursor: "pointer"}} onClick={() =>{*/}
            {/*                        setModalIsOpen(true);*/}
            {/*                        setCurrent(r);*/}
            {/*                        this.current = r;*/}
            {/*                    }*/}
            {/*                    }>{r}</p>*/}
            {/*                    <Rating*/}
            {/*                        name="ingredient-rating"*/}
            {/*                        precision={0.5}*/}
            {/*                        size={"small"}*/}
            {/*                        onChange={(event, newValue) => {*/}
            {/*                            rateIngredient(r, newValue);*/}
            {/*                        }}*/}
            {/*                        readOnly*/}
            {/*                        value={parseFloat(ingredientRatings[r])}*/}
            {/*                    />*/}
            {/*                </div>*/}
            {/*            )}*/}
            {/*        </div>*/}
            {/*    </div>*/}

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
            {/*</div>*/}
            <div>
            {/*<List x={600} width={800} label={"Add an Ingredient"} ingredients={[]}>*/}
            {/*    /!*original top number was 225*!/*/}
            {/*    <div style={{position: "relative", top: 100, left: 0, right:0}}>*/}
            {/*        <TextBox val={input} input={setInput} onKeyUp={createSuggestions}*/}
            {/*                 label={"Name of Ingredient"} setCurr={setCurrentToRate}/>*/}
            {/*        <h4 hidden={autocorrectLoading} className={"text-dark"}> Loading...</h4>*/}
            {/*        <ul aria-live={"polite"}>*/}
            {/*            {suggestions.map(item => {*/}
            {/*                return (*/}
            {/*                    <ListItem item={item} setInput={setInput} input={input} setCurr={setCurrentToRate} curr={currentToRate} />*/}
            {/*                )*/}
            {/*            })}*/}
            {/*        </ul>*/}
            {/*        /!*original top number was 50*!/*/}
            {/*        <div id={"submit"} style={{position: "relative", top: 0, left: 150}}>*/}
            {/*            /!*submission button*!/*/}
            {/*            <SubmitButton label={"Submit"} onClick={checkValidIngredient}/>*/}
            {/*            /!*this is for setting an error notification if ingredient is invalid*!/*/}
            {/*            <ToastContainer/>*/}
            {/*        </div>*/}
            {/*    </div>*/}
            {/*</List>*/}
            {/*textbox for input*/}
            </div>
        </div>
    );
}

export default Fridge;