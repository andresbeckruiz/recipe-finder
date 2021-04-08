import List from "./List";
import React, {useState, useEffect, useRef} from 'react';
import TextBox from "./TextBox";
import SubmitButton from "./SubmitButton";
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import {Link} from 'react-router-dom'

let list = [];

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

    // useState variable for text box input
    const [input, setInput] = useState("");

    // useState variable for ingredients list
    const [ingredients, setIngredients] = useState([""]);

    // useState variables for deletion modal
    const [modalIsOpen, setModalIsOpen] = useState(false);
    const [deleteIngredient, setDeleteIngredient] = useState(false);


    // style details for root page
    const rootStyle = {
        backgroundColor: "white",
        height: '100vh'
    }

    const handleClose = () => setModalIsOpen(false);
    const handleCloseDelete = () => {
        setDeleteIngredient(true);
        setModalIsOpen(false);
    };

    // function for submit button
    const onSubmit = () => {
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
    }

    //set global for listener
    submit = onSubmit;

    const onChange = () => {

    }

    return (
        <div style={rootStyle} className="Fridge">
            {/*dynamic header*/}
            <h1 style={{marginTop: 25}}>{name}'s Fridge</h1>
            {/*two buttons on side of page*/}
            {/*<Route exact path="/" component={Page1} />*/}
            {/*<Link to="/Recipe"><button>coom</button></Link>*/}
            <Link to={"/Recipe"}>
            <Button variant="success" size= "lg" style={{position: "absolute", left: 50, top: 25}}>Search for Recipes</Button>
            </Link>
            <Button variant="danger" size= "lg" style={{position: "absolute", right: 50, top: 25}}>Logout</Button>
            {/*two panes for lists and input*/}
            <List x={200} width={250} label={"Current Ingredients"} ingredients={ingredients} setter={setIngredients}
             setModalIsOpen={setModalIsOpen} deleteCurr={deleteIngredient} setDeleteCurr={setDeleteIngredient}/>

            {/*Modal for deletion*/}
            <Modal show={modalIsOpen} onHide={handleClose}>
                <Modal.Header closeButton>
                    <Modal.Title>Delete ingredient?</Modal.Title>
                </Modal.Header>
                <Modal.Body>This ingredient will deleted permanently from your Fridge.</Modal.Body>
                <Modal.Footer>
                    <Button variant="light" onClick={handleClose}>
                        Cancel
                    </Button>
                    <Button variant="danger" onClick={handleCloseDelete}>
                        Delete
                    </Button>
                </Modal.Footer>
            </Modal>

            <div>
            <List x={600} width={800} label={"Add an Ingredient"} ingredients={[]}>
                <div style={{position: "relative", top: 225, left: 0, right:0}}>
                    <TextBox input={setInput} change={onChange} label={"Name of Ingredient"}/>
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