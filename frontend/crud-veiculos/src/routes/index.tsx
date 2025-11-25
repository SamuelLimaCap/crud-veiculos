import {Suspense} from "react";
import {Route, Routes} from "react-router";
import PublicRoute from "./PublicRoute";
import Login from "../pages/public/Login/Login";
import Signup from "../pages/public/SignUp/Signup";

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
        </Routes>
    );
}