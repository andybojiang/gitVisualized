import React, { Component } from 'react'
import axios from 'axios'
import GitTerminal from './GitTerminal';
import Stage from './Stage'

class ApiConnection extends Component {
    constructor() {
        super()
        this.state = {
            address: 'http://localhost:8080/api/v1/git',
            UUID: '',
            fileStage: []
        }
        this.process = this.process.bind(this)
        this.getMap = this.getMap.bind(this)
        this.newFile = this.newFile.bind(this)
        this.printUUID = this.printUUID.bind(this)
        this.updateStage = this.updateStage.bind(this)
    }

    getMap(mapping) {
        return this.state.address + '/' + this.state.UUID + mapping
    }

    process(args, print, runCommand) {
        // TODO: Commit message bug
        args.shift()
        axios.post(this.getMap(''), { command: args }).then(response => {
            print(response.data)
        })
        this.updateStage()
        this.state.fileStage.map(item => {
            print(item.filename)
        })
    }

    componentDidMount() {
        axios.post(this.state.address).then(response => {
            this.setState({
                UUID: response.data
            })
        })
    }

    newFile(fileName, contents) {
        // TODO: post to add file
        axios.post(this.getMap('/file/add'), {
            filename: fileName,
            contents: contents
        })
        this.updateStage()
    }

    // For debugging
    printUUID(args, print, runCommand) {
        print(this.state.UUID)
    }

    updateStage() {
        console.log('updated?')
        axios.get(this.getMap('')).then(response => {
            console.log(response.data[0].contents)
            this.setState({
                fileStage: response.data
            })
        })
    }

    render() {
        window.addEventListener("beforeunload", (ev) => {
            axios.delete(this.getMap(''))
            ev.preventDefault()
            return ev.returnValue = 'Are you sure you want to close?'
        })
        return (
            <div>
                <GitTerminal
                    process = {this.process}
                    getter = {this.getter}
                />
                <Stage
                    fileStage = {this.state.fileStage}
                    newFile = {this.newFile}
                    // TODO: Pass down stage elements, addFile and deleteFile
                />
            </div>
        )
    }
}

export default ApiConnection