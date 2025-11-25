import {ReactNode} from "react";
import {useAuth} from "../hooks/useAuth";
import {Navigate} from "react-router";

interface ProtectedRouteProps {
    permission:string | string[];
    children: ReactNode;
}

const checkAccess = (value: string | string[], permissions: string | string[] ):boolean  => {
    if (!value) return true;

    const valueArray= Array.isArray(value) ? value : [value];
    const permissionArray = Array.isArray(permissions) ? permissions : [permissions];

    return valueArray.every((perm) => permissionArray.includes(perm))
}
export default function ProtectedRoute({permission, children}: ProtectedRouteProps) {
    const {user} = useAuth();

    if (!user !! ) {
        return <Navigate to="/" replace />
    }

    const hasPermission = checkAccess(permission, user?.permissions)

    if (!hasPermission)
        return <Navigate to={"/"} replace />

    return children;

}
