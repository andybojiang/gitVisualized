import React, { useState, useEffect } from 'react'
import ApiConnection from './ApiConnection'


import './App.css';


class App extends React.Component {
  // This wrapper layer is unnecessary, will get rid of
  constructor(props) {
    super(props)
  }
  render() {
    return (
      <>
        <link href="https://fonts.googleapis.com/css2?family=Roboto:ital,wght@0,100;0,300;0,400;0,500;0,700;0,900;1,100;1,300;1,400;1,500;1,700;1,900&display=swap" rel="stylesheet" />
        <ApiConnection />
      </>
    );
  }
}



export default App;
