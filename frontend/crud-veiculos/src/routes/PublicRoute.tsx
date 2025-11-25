import {JSX} from "react";
import {useAuth} from "../hooks/useAuth";

interface PublicRouteProps {
    children: JSX.Element,
    redirectWhenAuthenticated?: boolean,
}

export default function PublicRoute({
    children,
    redirectWhenAuthenticated,
                                    }: PublicRouteProps) {
    const {user, loading, navigateToHomePage} = useAuth()

    if (user && redirectWhenAuthenticated) {
        navigateToHomePage()
        return null;
    }


    return children
}