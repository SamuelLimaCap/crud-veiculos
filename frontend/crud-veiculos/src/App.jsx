import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'
import {BrowserRouter} from "react-router";
import {AuthProvider} from "./contexts/AuthContext.tsx";
import {AppRoutes} from "./routes/index.tsx";

function App() {

  return (
      <BrowserRouter>
          <AuthProvider>
              <AppRoutes />
          </AuthProvider>
      </BrowserRouter>
  )
}

export default App
