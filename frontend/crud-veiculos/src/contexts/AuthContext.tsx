import { createContext, ReactNode, useCallback, useEffect, useMemo, useState } from "react";
import { useNavigate } from "react-router";
import { API } from "../services/api";
import { permission } from "process";


interface AuthProviderProps {
    children: ReactNode;
}

interface SignInProps {
    email: string;
    password: string;
}

interface SignUpProps {
    fullname: string;
    email: string;
    password: string;
}

interface User {
    idUser: String,
    fullname: string,
    email: string,
    permissions: string[]
}

interface AuthContextProps {
    loading: boolean,
    user: User | null,
    signIn: (signInData: SignInProps) => Promise<void>,
    signUp: (signUpData: SignUpProps) => Promise<void>,
    signOut: () => Promise<void>,
    navigateToHomePage: () => Promise<void>,

}

export const AuthContext = createContext<AuthContextProps>({
    user: null,
    loading: true,
    signIn: async () => {
    },
    signUp: async () => {
    },
    signOut: async () => {
    },
    navigateToHomePage: async () => {
    },
})

export const AuthProvider = ({ children }: AuthProviderProps) => {
    const navigate = useNavigate();

    const [user, setUser] = useState<User | null>(null);
    const [loading, setLoading] = useState(false);


    useEffect(() => {
        if (!localStorage.getItem("accessToken")) {
            setUser(null);
            return;
        }
        if (isTokenExpired(localStorage.getItem("accessToken")!!)) {
            setUser(null);
            return;
        }
        if (!localStorage.getItem("username")) {
            setUser(null);
            return;
        }

        const userFromLocalStorage = {
            "idUser": localStorage.getItem("idUser") ?? "none",
            "fullname": localStorage.getItem("username") ?? "none",
            "email": localStorage.getItem("email") ?? "none",
            "permissions": JSON.parse(localStorage.getItem("permissions") ?? "none"),
        } as User;
        setUser(userFromLocalStorage)
        return;

    }, []);

    function isTokenExpired(token: string): boolean {
        try {
            const payload = JSON.parse(atob(token.split('.')[1]));
            const expirationTime = payload.exp * 1000; // Convert to milliseconds
            return Date.now() >= expirationTime;
        } catch (error) {
            return true; // If decoding fails, consider it expired
        }
    }

    const navigateToHomePage = useCallback(async () => {
        navigate("/home")
    }, [navigate])

    const signOut = useCallback(async () => {
        setUser(null)
        navigate("/")
    }, [navigate])

    const signIn = useCallback(async ({ email, password }: SignInProps) => {

        var data = {
            "email": email,
            "password": password
        }
        var response = await API.post("auth/signin", data)
        var dataObjectResponse = response.data;

        if (dataObjectResponse.status == "success") {
            localStorage.setItem("accessToken", dataObjectResponse.content.accessToken)
            localStorage.setItem("refreshToken", dataObjectResponse.content.refreshToken)
            if (dataObjectResponse.content.userDetails) {
                var user = dataObjectResponse.content.userDetails
                localStorage.setItem("idUser", user.idUser)
                localStorage.setItem("username", user.fullName)
                localStorage.setItem("email", user.email)
                localStorage.setItem("permissions", JSON.stringify(user.permissions))
                setUser(user);

                navigate("/home")
            }
        }
    }, [])

    const signUp = useCallback(
        async ({ fullname, email, password }: SignUpProps) => {

            var data = {
                "fullname": fullname,
                "email": email,
                "password": password
            }

            var response = await API.post("auth/signup", data)
            var dataObjectResponse = response.data;
            if (dataObjectResponse.status === "success") {
                // @Todo: toast success
                navigate("/")
            }

            //@Todo: cadastrar service

            //@Todo: Redirecionar pro login

        }, []
    )

    const values = useMemo(
        () => ({
            user, loading, signIn, signUp, signOut, navigateToHomePage
        }),
        [user, loading, signIn, signUp, signOut, navigateToHomePage]
    )

    return <AuthContext.Provider value={values}>{children}</AuthContext.Provider>
}
