import React, {useState, useEffect} from 'react';
import './App.css';
import axios from "axios";

function ApiConnection() {
  //useState hook that defines a state userProfiles and a f() setUserProfiles that updates the state
  const [s, setS] = useState([]);

  const fetchInfo = () => {
    axios.post("http://localhost:8080/api/v1/git").then(res => {//GET request to backend, .then handles data sent back
      console.log(res);
      setS(res.data);//what is res.data exactly?
    });


  };

  useEffect(() => {//example of use effect
    fetchInfo();
  }, []);

  return(
    <div>
      
      <h1>{s}</h1>
    </div>
  )
};

function App() {
  return (
    <div className="App">
      <ApiConnection/>
    </div>
  );
}

export default App;
