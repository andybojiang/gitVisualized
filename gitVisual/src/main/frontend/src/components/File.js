import React from 'react'

function File(props) {
    const style = {}

    if (props.file.status === 0) {
        style.borderColor = "gray"
    } else if (props.file.status === 1) {
        style.borderColor = "red"
    } else {
        style.borderColor = "green"
    }

    return (
        <div className="file" style={style}>
            <button className="x-button" onClick={() => props.onDelete()}>X</button>
            <p className="file-name">{props.file.filename}</p>
            <p className="file-contents">{props.file.contents}</p>
        </div>
    )
}

export default File