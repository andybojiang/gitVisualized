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

    // newFile(promptName, promptContent) {
    //     // NEW FILE TO BACKEND USING PROPS//
    //     this.props.newFile(promptName, promptContent)
    //     // this.setState({
    //     //     stageArea: [
    //     //         ...this.state.stageArea,
    //     //         { id: fileIndex += 1, name: promptName, contents: promptContent, added: false }
    //     //     ]
    //     // })
    // }

    // addFile(filename) {
    //     this.setState({
    //         stageArea: this.state.stageArea.map(stageItem => {
    //             if (filename === stageItem.name) {
    //                 stageItem.added = !stageItem.added
    //             }
    //             return stageItem
    //         })
    //     })
    // }

    render() {
        return (
            <>
                <h1>CWD</h1>
                <button
                    className="add-file-button"
                    onClick={() => this.props.newFile(
                        prompt("enter a file name"), prompt("enter the file contents"))}>
                    new file
                </button>
                <div className="stage-area">
                    {/* {this.state.stageArea.map(stageItem => (
                        <File
                            key = {stageItem.id}
                            onDelete={() => this.deleteFile(stageItem.name)}
                            file={stageItem}
                        />
                    ))} */}
                    {this.props.fileStage.map(item => (
                        <File 
                            key = {item.id}
                            onDelete = {() => this.props.deleteFile(item.filename, item.contents)}
                            file = {item}
                        />
                    ))}
                </div>
            </>
        )
    }
}



export default Stage