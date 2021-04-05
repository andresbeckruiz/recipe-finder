import { AwesomeButton } from "react-awesome-button";
import "react-awesome-button/dist/styles.css";

function SubmitButton(props) {

    return <AwesomeButton type="primary" onPress= {
        // click
        props.onClick

    }>{props.label}!</AwesomeButton>;
}

export default SubmitButton;