import React, {useState, useEffect} from 'react';
import Rating from "@material-ui/lab/Rating";
import './List.css';
let current;

let ingredients = [];
function List(props) {

    // useState hooks for list and flag
    const [list, setList] = useState({});
    const [flag, setFlag] = useState(0);


    const style = {
        backgroundColor: "#2776ED",
        height: 600,
        width: props.width,
        position: "absolute",
        top: 150,
        left: props.x
    }

    const innerStyle = {
        height: 590,
        width: props.width,
        position: "absolute",
        bottom: 10,
        overflow: "auto"
    }

    function deleteCurrent() {
        let curr = props.ingredients;
        delete curr[current];
        props.setter(curr);
        setFlag(flag+1);
        props.setDeleteCurr(false);
    }

    // useEffect hook for ingredient list updates
    useEffect(() => {
        setList(props.ingredients);
        ingredients = props.list;
    }, [props.ingredients, flag])

    useEffect(() => {
        if(props.deleteCurr) {
            deleteCurrent();
        }
    }, [props.deleteCurr])

    return (
        <div style={style} className="List">
            <h4 style={{position: "absolute", top: -40}}>{props.label}</h4>
            <div style={innerStyle} className="List">
                <div style={{marginTop: 25}}>
                {Array.from(Object.keys(list)).map(r =>

                   <div key={r} className="ingredient">

                       <p style={{textAlign: "center", cursor: "pointer"}} onClick={() =>{
                        props.setModalIsOpen(true);
                        props.setCurrent(r);
                        current = r;
                        }
                       }>{r}</p>
                       <Rating
                           name="ingredient-rating"
                           precision={0.5}
                           size={"small"}
                           onChange={(event, newValue) => {
                               let test = list;
                               test[r] = newValue;
                               setList(test);
                               props.ingredientRater(r, newValue);
                           }}
                           readOnly
                           value={parseFloat(list[r])}
                       />
                   </div>
                )}
                </div>
            </div>
            {props.children}
        </div>
    );
}

export default List;