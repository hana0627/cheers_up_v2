import './App.css';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Index from "./pages/Index";
import PubSearch from "./pages/pub/PubSearch";
import PubList from "./pages/pub/PubList";



function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Index/>} />
        <Route path="/PubSearch" element={<PubSearch/>} />
        <Route path="/PubList" element={<PubList/>} />
      </Routes>
    </Router>
  );
}


export default App;
