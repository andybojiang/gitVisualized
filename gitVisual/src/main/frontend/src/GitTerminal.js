import React, {Component} from 'react'
import Terminal from 'terminal-in-react'

// git-init <args>


class GitTerminal extends Component {
    constructor(props) {
        super(props)
    }

    render() {
        return (
            <Terminal
                color='green'
                backgroundColor='black'
                barColor='black'
                style={{ fontWeight: "bold", fontSize: "1em" }}
                commandPassThrough={cmd => `-not a git repository or not a valid command:${cmd}: `}
                msg='Type help to see all commands'
                commands={{
                    'git': this.props.process,
                    
                    'git init': '',
                    'git add <file name>': '',
                    'git rm <file name>': '',
                    'git commit <message>': '',
                }}
                descriptions={{
                    'git': false,

                    'git init': 'Create an empty Git repository',
                    'git add <file name>': 'Add a file to the staging area',
                    'git rm <file name>': 'Remove a file from the index',
                    'git commit <message>': 'Record changes to the repository', 

                    'color': false,
                    'show': false,
                }}
            />
        )
    }
}


export default GitTerminal