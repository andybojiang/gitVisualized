import React from 'react'
import Terminal from 'terminal-in-react'

function GitTerminal(props) {

    return (

        <>
        <link href="https://fonts.googleapis.com/css2?family=Source+Code+Pro:wght@300;500&display=swap" rel="stylesheet"></link>
        <Terminal
            color='green'
            backgroundColor='black'
            barColor='black'
            style={{ fontFamily: 'Source Code Pro', fontSize: "1em" }}
            commandPassThrough={cmd => `Not a git repository or not a valid command:${cmd}: `}
            msg='Type help to see all commands'
            commands={{
                // 'UUID': props.printUUID,
                'git init': '',
                'git add <file name>': '',
                'git rm <file name>': '',
                'git commit <message>': '',
                'git command <operands>': '',
                
                'git': props.process,
            }}
            descriptions={{
                'git init': 'Create an empty Git repository',
                'git add <file name>': 'Add a file to the staging area',
                'git rm <file name>': 'Remove a file from the index',
                'git commit <message>': 'Record changes to the repository',
                'git commad <operands': 'A template to create more git commands',
                
                'git': false,
                'color': false,
                'show': false,
            }}
        />
        </>
    )

}


export default GitTerminal