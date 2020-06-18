import React, { Component } from 'react';
import CytoscapeComponent from 'react-cytoscapejs';

function Branch(x, y) {
    this.y = y
    this.x = x
    this.getPos = function () {
        this.x += 100
        return { x: this.x, y: this.y * 100 }
    }
}

class Graph extends Component {
    constructor(props) {
        super(props);
        let branches = {}
        branches['master'] = new Branch(0, 0)
        this.state = {
            headBranch: 'master',
            nextY: 0,
            nodes: [],
            edges: [],
            branches: branches,
        }
        this.addBranch = this.addBranch.bind(this)
        this.checkout = this.checkout.bind(this)
        this.addNode = this.addNode.bind(this)
        this.addEdge = this.addEdge.bind(this)
        this.getY = this.getY.bind(this)

        this.commit = this.commit.bind(this)
    }

    getY() {
        const sign = (this.state.nextY === 0 ? -1 : Math.sign(this.state.nextY))
        // const adjustedY = this.state.nextY - (sign * Object.keys(this.state.branches).length)
        const adjustedY = this.state.branches.length
        // this.setState({
        //     nextY: adjustedY
        // })
        return adjustedY
    }


    addBranch() {
        const name = prompt('gimme a name')
        const newx = this.state.branches[this.state.headBranch].x
        const newy = Object.keys(this.state.branches).length
        console.log('newY: ' + newy)
        let updatedBranches = this.state.branches
        updatedBranches[name] = new Branch(newx, newy)
        this.setState({
            branches: updatedBranches
        })
    }

    checkout() {
        const newHead = prompt('which branch')
        if (this.state.branches[newHead] === undefined) {
            console.log('trapped?')
            return
        }
        this.setState({
            headBranch: newHead
        })
    }

    addNode() {
        const label = prompt('gimme a name')
        this.setState({
            nodes: [
                ...this.state.nodes,
                {
                    data: { id: label, label: "Node: " + label },
                    position: this.state.branches[this.state.headBranch].getPos(),
                    locked: false,
                    metadatea: 'heres some data'
                }
            ],
        })
    }

    addEdge() {
        if (this.state.nodes.length < 2) return
        const lastNode = this.state.nodes.slice(-1)[0]
        const secondLastNode = this.state.nodes.slice(-2)[0]
        this.setState({
            edges: [
                ...this.state.edges,
                {
                    data: {
                        source: lastNode.data.id,
                        target: secondLastNode.data.id
                    }
                }
            ]
        })
    }

    commit() {
        const label = prompt('gimme a name')
        this.setState({
            nodes: [
                ...this.state.nodes,
                {
                    data: { id: label, label: "Node: " + label },
                    position: this.state.branches[this.state.headBranch].getPos(),
                    locked: false,
                    metadatea: 'heres some data'
                }
            ],
        })
    }

    stylesheet() {
        return [ // the stylesheet for the graph
            {
                selector: 'node',
                style: {
                    'background-color': '#666',
                    'label': 'data(label)'
                }
            },

            {
                selector: 'edge',
                style: {
                    'width': 3,
                    'line-color': '#ccc',
                    'target-arrow-color': '#ccc',
                    'target-arrow-shape': 'triangle',
                    'curve-style': 'bezier'
                }
            }
        ]
    }

    render() {
        return (
            <>
                <button onClick={this.addNode}>add node</button>
                <button onClick={this.addEdge}>add edge</button>
                <button onClick={this.addBranch}>add branch</button>
                <button onClick={this.checkout}>checkout</button>
                <h1>Auto</h1>
                <button onClick={this.commit}>commit</button>
                <CytoscapeComponent
                    elements={CytoscapeComponent.normalizeElements({
                        nodes: this.state.nodes,
                        edges: this.state.edges
                    })}
                    style={{ width: '600px', height: '600px' }}
                    stylesheet={this.stylesheet()}
                />
            </>
        )
    }
}

export default Graph;
