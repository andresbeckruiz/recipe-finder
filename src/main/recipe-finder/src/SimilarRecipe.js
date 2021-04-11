
function SimilarRecipe(props) {

    let style = {
        backgroundColor: "#888",
        height: 200,
        width: 200
    }

    return (
        <div style={style} className="flex-item">
            <h5 style={{marginTop: 80}}>{props.label}</h5>
        </div>
    );
}

export default SimilarRecipe;