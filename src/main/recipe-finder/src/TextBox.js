
function TextBox(props) {

    // Changes value of text box
    const value = (event) => {
        props.change(event.target.value);
    }

    // Style of label
    const labelStyle = {
        fontSize: 20,
        fontFamily: "Avenir",
        textAlign: "left",
        position: "relative",
        left: -125,
        bottom:1
    }

    return (
        <div>
            <label style={labelStyle}>{props.label}</label>
            <br/>
            <input style={{fontSize:24}} className="inputBox" type={"text"} onChange={value}></input>
        </div>
    );
}

export default TextBox;
