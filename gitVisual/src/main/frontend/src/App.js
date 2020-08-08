import React, { useContext } from 'react'
import ApiConnection from './components/ApiConnection'


import './App.css';
import { useContext } from 'react';
import GitTerminal from './components/GitTerminal';
import Graph from './components/Graph';


class App extends React.Component {
  // This wrapper layer is useless, will get rid of

  render() {
    return (
      <>
        <link href="https://fonts.googleapis.com/css2?family=Roboto:ital,wght@0,100;0,300;0,400;0,500;0,700;0,900;1,100;1,300;1,400;1,500;1,700;1,900&display=swap" rel="stylesheet" />
        <ApiConnection />
      </>
    );
  }
}

function App() {
  return (
    <APIContext>
      <GitTerminal />
      <Graph />
      <Stage />
    </APIContext>
  )
}



export default App;
