
function TextBox(props) {

    // Changes value of text box
    const value = (event) => {
        props.input(event.target.value);
    }

    // Style of label
    const labelStyle = {
        fontSize: 20,
        fontFamily: "Avenir",
        textAlign: "left",
        position: "relative",
        left: 0,
        bottom:0
    }

    return (
        <div>
            <label style={labelStyle}>{props.label}</label>
            <br/>
            <input style={{fontSize:24}} className="inputBox" type={"text"} onChange={value} id={"inputBox"}></input>
        </div>
    );
}

export default TextBox;
