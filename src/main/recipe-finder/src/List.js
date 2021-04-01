
function List(props) {

    let style = {
        backgroundColor: "#2776ED",
        height: 600,
        width: props.width,
        position: "absolute",
        top: 150,
        left: props.x
    }

    return (
        <div style={style} className="List">
            <h2 style={{position: "relative", top: -60, left: 0, right: 0}}>{props.label}</h2>
        </div>
    );
}

export default List;