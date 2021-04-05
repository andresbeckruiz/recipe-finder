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
        console.log("triggerd");
        console.log(props.ingredients);
        setList(props.ingredients);
    }, [props.ingredients, flag])

    return (
        <div style={style} className="List">
            <h2 style={{position: "relative", top: -60, left: 0, right: 0}}>{props.label}</h2>
            <div style={innerStyle} className="List">
                <br/>
                {list.map((r) =>
                    <p onClick={() =>{
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
    );
}

export default List;