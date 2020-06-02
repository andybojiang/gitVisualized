import React from 'react'
import File from './File'

let fileIndex = 0

class Stage extends React.Component {
    constructor(props) {
        super(props)
        this.state = {
            stageArea: [],
        }
    }

    // MAJOR TODO: Convert these to actual calls to backend and just have this class render whatever props.fileStage passes down

    newFile(promptName, promptContent) {
        // ADD A FILE TO BACKEND USING PROPS//
        this.setState({
            stageArea: [
                ...this.state.stageArea,
                { id: fileIndex += 1, name: promptName, contents: promptContent, added: false }
            ]
        })
    }

    addFile(fileName) {
        // ADDS FILE TO BACKEND STAGE //
        this.setState({
            stageArea: this.state.stageArea.map(stageItem => {
                if (fileName === stageItem.name) {
                    stageItem.added = !stageItem.added
                }
                return stageItem
            })
        })
    }

    removeFile(itemID) {
        // REMOVE FROM BACKEND USING PROPS//
        this.setState({
            stageArea: this.state.stageArea.filter(stageItem => stageItem.id !== itemID),
        })
    }

 

    render() {
        return (
            <>
                <h1>CWD</h1>
                <button
                    className="add-file-button"
                    onClick={() => this.newFile(
                        prompt("enter a file name"), prompt("enter the file contents"))}>
                    new file
                </button>
                <button
                    className="add-file-button"
                    onClick={() => this.addFile(
                        prompt("enter a file name"))}>
                    add file
                </button>
                <div className="stage-area">
                    {/* LOAD FILES FROM PROPS INSTEAD */}
                    {this.state.stageArea.map(stageItem => (
                        <File
                            key = {stageItem.id}
                            onDelete={() => this.removeFile(stageItem.id)}
                            file={stageItem}
                        />
                    ))}
                </div>
            </>
        )
    }
}



export default Stage