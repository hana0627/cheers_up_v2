import './App.css';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Index from "./pages/Index";
import PubSearch from "./pages/pub/PubSearch";
import PubList from "./pages/pub/PubList";
import {AuthProvider} from "./pages/context/AuthContext";
import ProtectedRoute from "./pages/ProtectedRoute";
import ErrorPage404 from "./pages/error/ErorPage404r";



function App() {
  return (
    <AuthProvider>
      <Router>
        <Routes>
          <Route path="/" element={<Index/>}/>
          <Route path="/PubSearch" element={
            <ProtectedRoute>
              <PubSearch/>
            </ProtectedRoute>
          } />
          <Route path="/PubList" element={
            <ProtectedRoute>
              <PubList/>
            </ProtectedRoute>
          } />
          <Route path="*" element={<ErrorPage404/>}></Route>
        </Routes>
      </Router>
    </AuthProvider>

  );
}


export default App;
