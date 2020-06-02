import React from 'react'

function File(props) {
    const style = {
        borderColor: "red"
    }

    if (props.file.added) {
        style.borderColor = "green"
    }

    return (
        <div className="file" style={style}>
            <button className="x-button" onClick={() => props.onDelete()}>X</button>
            <p className="file-name">{props.file.name}</p>
            <p className="file-contents">{props.file.contents}</p>
        </div>
    )
}

export default File