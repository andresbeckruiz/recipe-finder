import React from 'react';

/**
 * Need this class because we want to set the state to the item that it displays when clicked
 * @param props
 * @returns {JSX.Element}
 * @constructor
 */
function ListItem(props) {

    const replaceInput = () => {
        let newInput = props.item;
        props.setInput(newInput);
        props.setCurr(newInput);
        console.log(props.item);
        console.log(props.input);
        console.log(props.curr);
    }

    return (
        <li tabIndex={0} onClick={replaceInput}> {props.item}</li>
    );
}

export default ListItem;