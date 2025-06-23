import logo from './logo.svg';
import './App.css';
import {HeroUIProvider} from "@heroui/react";
import FileList from "./components/FileList";
import FileUploadButton from "./components/FileUploadButton";

function App() {
  return (
      <HeroUIProvider>
    {/*<div className="App">*/}
    {/*  <header className="App-header">*/}
    {/*    <img src={logo} className="App-logo" alt="logo" />*/}
    {/*    <p>*/}
    {/*      Edit <code>src/App.js</code> and save to reload.*/}
    {/*    </p>*/}
    {/*    <a*/}
    {/*      className="App-link"*/}
    {/*      href="https://reactjs.org"*/}
    {/*      target="_blank"*/}
    {/*      rel="noopener noreferrer"*/}
    {/*    >*/}
    {/*      Learn React*/}
    {/*    </a>*/}
          <FileList/>
          <FileUploadButton />
    {/*  </header>*/}
    {/*</div>*/}
      </HeroUIProvider>
  );
}

export default App;
