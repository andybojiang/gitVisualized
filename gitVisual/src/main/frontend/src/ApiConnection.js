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
        this.deleteFile = this.deleteFile.bind(this)
    }

    getMap(mapping) {
        return this.state.address + '/' + this.state.UUID + mapping
    }

    process(args, print, runCommand) {
        // TODO: Commit message bug
        this.updateStage()
        args.shift()
        if (args[0] === 'commit' && args.length > 1) {
            let lastWord = args[args.length - 1]
            if (args[1].charAt(0) !== '"' || lastWord.charAt(lastWord.length - 1) !== '"') {
                print('commit message must be wrapped in quotations "like this"')
                return
            } else {
                let message = ""
                for (var i = 1; i < args.length; i += 1) {
                    message += " " + args[i]
                }
                args[1] = message
                args.splice(2, args.length)
            }
        }
        axios.post(this.getMap(''), { command: args }).then(response => {
            print(response.data)
        })
        this.updateStage()
        this.updateStage()
        // this.state.fileStage.map(item => {
        //     print(item.filename)
        // })
    }

    combineMessage(args) {
        if (args[0] === 'commit' && args.length > 1) {
            let lastWord = args[args.length - 1]
            if (args[1].charAt(0) !== '"' || lastWord.charAt(lastWord.length - 1) !== '"') {
                // print('commit message must be wrapped in quotations "like this"')
                return
            } else {
                let message = ""
                for (var i = 1; i < args.length; i += 1) {
                    message += " " + args[i]
                }
                args[1] = message
                args.splice(2, args.length)
            }
        }
    }

    componentDidMount() {
        axios.post(this.state.address).then(response => {
            this.setState({
                UUID: response.data
            })
        })
    }

    newFile() {
        // TODO: post to add file
        this.updateStage()

        let fileName = prompt('Enter a file name')
        while (fileName.split(' ').length > 1) {
            fileName = prompt('File name must be one word')
        }
        let contents = prompt('Enter the file contents')

        axios.post(this.getMap('/file/add'), {
            filename: fileName,
            contents: contents
        })
        this.updateStage()
        this.updateStage()
        // this.updateStage()
        // this.updateStage()
    }

    deleteFile(fileName, contents) {
        this.updateStage()
        axios.delete(this.getMap('/file'), {
            filename: fileName,
            contents: contents
        })
        this.updateStage()
        this.updateStage()
    }

    // For debugging
    printUUID(args, print, runCommand) {
        print(this.state.UUID)
    }

    updateStage() {
        console.log('updating...')
        axios.get(this.getMap('')).then(response => {
            this.setState({
                fileStage: response.data
            })
        })
        axios.post(this.getMap(''), { command: 'git' }).then(response1 => {
            console.log(response1.data)
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
                    deleteFile = {this.deleteFile}
                    // TODO: Pass down stage elements, addFile and deleteFile
                />
            </div>
        )
    }
}

export default ApiConnection