import React from 'react';
import './App.css';
import Logo from './logo.png'
import DecisionForm from './DecisionForm';

const App: React.FC = () => {
    return (
        <div className="App">
            <header className="App-header">
                <img src={Logo} className="App-logo" alt="logo" />
                <h1>Decision App</h1>
            </header>
            <main>
                <DecisionForm />
            </main>
        </div>
    );
}

export default App;
