import React from 'react';

/**
 * Need this class because we want to set the state to the item that it displays when clicked
 * @param props
 * @returns {JSX.Element}
 * @constructor
 */
function ListItem(props) {

    const replaceInput = () => {
        props.setInput(props.item)
    }

    return (
        <li tabIndex={0} onClick={replaceInput}> {props.item}</li>
    );
}

export default ListItem;