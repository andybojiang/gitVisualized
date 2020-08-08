import React, { useContext, useEffect } from 'react'
import axios from 'axios'

export default APIContext = useContext(null)

export function APIProvider(children) {

    async function process(args, print, runCommand) {
        // TODO: Commit message bug
        // This update satage behavior is so weird
        // Needs one before and two after to work
        updateStage()
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
        updateStage()
        updateStage()
        // this.state.fileStage.map(item => {
        //     print(item.filename)
        // })
    }

    async function newFile() {
        // TODO: post to add file
        updateStage()

        let fileName = prompt('Enter a file name')
        while (fileName.split(' ').length > 1) {
            fileName = prompt('File name must be one word')
        }
        let contents = prompt('Enter the file contents')

        axios.post(this.getMap('/file/add'), {
            filename: fileName,
            contents: contents
        })
        updateStage()
        updateStage()
        // this.updateStage()
        // this.updateStage()
    }

    async function updateStage() {
        console.log('updating stage...')
        axios.get(this.getMap('')).then(response => {
            this.setState({
                fileStage: response.data
            })
        })
        axios.post(this.getMap(''), { command: 'git' }).then(response1 => {
            console.log(response1.data)
        })
    }

    // on component did mount
    useEffect(() => {
        axios.post(this.state.address).then(response => {
            this.setState({
                UUID: response.data
            })
        })
    }, [])

    return (
        <APIContext.provider
            value = {{
                nodes: null,
                edges: null,
                process: process,
                newFile: newFile,
            }}
        >
            {children}
        </APIContext.provider>
    )

}