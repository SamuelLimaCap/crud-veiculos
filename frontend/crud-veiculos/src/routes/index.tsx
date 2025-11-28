import {Suspense} from "react";
import {Route, Routes} from "react-router";
import PublicRoute from "./PublicRoute";
import Login from "../pages/public/Login/Login";
import Signup from "../pages/public/SignUp/Signup";
import ProtectedRoute from "./ProtectedRoute";
import Lista from "../pages/Carros/Lista";
import AnuncioByID from "../pages/Carros/anuncioId";
import Anunciar from "../pages/Carros/Anunciar/Anunciar";

export function AppRoutes() {
    return (
        <Routes>
            <Route
                path="/"
                element={
                    <PublicRoute redirectWhenAuthenticated={true}>
                        <Login/>
                    </PublicRoute>
                } />
            <Route
                path="/signup"
                element={
                    <PublicRoute redirectWhenAuthenticated={true}>
                        <Signup/>
                    </PublicRoute>
                } />

                {
                //Rotas Protegidas
                }
            <Route
                path="/home"
                element={
                    <ProtectedRoute permission={"CLIENT"}>
                        <Lista />
                    </ProtectedRoute>

                } />

                <Route 
                path="/anuncio/{id}"
                element={
                    <ProtectedRoute permission={"CLIENT"}>
                        <AnuncioByID id="1" />
                    </ProtectedRoute>
                }
                />

                <Route 
                path="/anunciar"
                element={
                    <ProtectedRoute permission={"CLIENT"}>
                        <Anunciar />
                    </ProtectedRoute>
                }
                />
        </Routes>
    );
}