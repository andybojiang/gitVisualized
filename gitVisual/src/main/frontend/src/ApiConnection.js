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
        this.addFile = this.addFile.bind(this)
        this.getMap = this.getMap.bind(this)
    }

    getMap() {
        return this.state.address + '/' + this.state.UUID
    }


    process(args, print, runCommand) {
        // TODO: Commit message bug
        // Debug messages:
        // print('running process: ' + args)
        // print('mapping: ' + this.getMap())
        args.shift()
        axios.post(this.getMap(), { command: args }).then(response => {
            print(response.data)
        })
    }

    componentDidMount() {
        axios.post(this.state.address).then(response => {
            this.setState({
                UUID: response.data
            })
        })
    }

    addFile() {
        // TODO: post to add file
    }


    render() {
        window.addEventListener("beforeunload", (ev) => {
            axios.delete(this.getMap())
            ev.preventDefault()
            return ev.returnValue = 'Are you sure you want to close?'
        })
        return (
            <div>
                <GitTerminal
                    process={this.process}
                />
                <Stage
                    fileStage={this.state.fileStage}
                    addFile={this.addFile}
                    // TODO: Pass down stage elements, addFile and deleteFile
                />
            </div>
        )
    }
}

export default ApiConnection