import React, {useState, useEffect, useRef} from 'react';

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
        height: 600,
        width: props.width,
        position: "absolute",
        bottom: 0,
        overflow: "auto"
    }

    // useEffect hook for ingredient list updates
    useEffect(() => {
        setList(props.ingredients);
    }, [props.ingredients, flag])

    return (
        <div style={style} className="List">
            <h4 style={{position: "absolute", top: -40}}>{props.label}</h4>
            <div style={innerStyle} className="List">
                <div style={{marginTop: 25}}>
                {list.map((r) =>
                    <p style={{textAlign: "center"}} onClick={() =>{
                        let list = props.ingredients;
                        for(let i = 0; i < list.length; i++){

                            if (list[i] === r) {
                                list.splice(i, 1);
                            }
                        }
                        props.setter(list);
                        setFlag(flag+1);
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