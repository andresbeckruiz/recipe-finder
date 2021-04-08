import React, {useState, useEffect, useRef} from 'react';

let current;

function List(props) {

    // useState hooks for list and flag
    const [list, setList] = useState([]);
    const [flag, setFlag] = useState(0);


    let style = {
        backgroundColor: "#2776ED",
        height: 600,
        width: props.width,
        position: "absolute",
        top: 150,
        left: props.x
    }

    let innerStyle = {
        height: 590,
        width: props.width,
        position: "absolute",
        bottom: 10,
        overflow: "auto"
    }

    function deleteCurrent() {
        let list = props.ingredients;
        for(let i = 0; i < list.length; i++){

            if (list[i] === current) {
                list.splice(i, 1);
            }
        }
        props.setter(list);
        setFlag(flag+1);
        props.setDeleteCurr(false);
    }

    // useEffect hook for ingredient list updates
    useEffect(() => {
        setList(props.ingredients);
    }, [props.ingredients, flag])

    useEffect(() => {
        console.log("caught");
        if(props.deleteCurr) {
            deleteCurrent();
        }
    }, [props.deleteCurr])

    return (
        <div style={style} className="List">
            <h4 style={{position: "absolute", top: -40}}>{props.label}</h4>
            <div style={innerStyle} className="List">
                <div style={{marginTop: 25}}>
                {list.map((r) =>
                    <p style={{textAlign: "center", cursor: "pointer"}} onClick={() =>{
                        props.setModalIsOpen(true);
                        current = r;
                    }
                    }>{r}</p>
                )}
                </div>
            </div>
            {props.children}
        </div>
    );
}

export default List;