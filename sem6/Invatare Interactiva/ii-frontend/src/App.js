import React from 'react';
import './App.css';
import AuthForm from "./components/Auth/AuthForm";
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { AuthContextProvider } from "./components/Auth/auth-context";
import ProtectedRoute from './components/Auth/ProtectedRoute';
import MainPage from './pages/MainPage';

const App = () => {
  return (
    <BrowserRouter>
      <Routes>
        <Route element={<AuthContextProvider />}>
          <Route path="/" element={<AuthForm />} />
          <Route element={<ProtectedRoute />}>
            <Route path="/mainPage" element={<MainPage />} />
          </Route>
        </Route>
      </Routes>
    </BrowserRouter>
  );
};

export default App;

