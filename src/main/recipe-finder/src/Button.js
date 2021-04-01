import { AwesomeButton } from "react-awesome-button";
import "react-awesome-button/dist/styles.css";

function Button(props) {

    return <AwesomeButton type="primary" onPress= {
        // click
        props.onClick

    }>{props.label}!</AwesomeButton>;
}

export default Button;