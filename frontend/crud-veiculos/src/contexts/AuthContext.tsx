import {createContext, ReactNode, useCallback, useMemo, useState} from "react";
import {useNavigate} from "react-router";



interface AuthProviderProps {
    children: ReactNode;
}

interface SignInProps {
    email: string;
    password: string;
}

interface SignUpProps {
    name: string;
    email: string;
    password: string;
}

interface User {
    name: string,
    permissions: string[]
}

interface AuthContextProps {
    loading: boolean,
    user: User | null,
    signIn : (signInData: SignInProps) => Promise<void>,
    signUp : (signUpData: SignUpProps) => Promise<void>,
    signOut : () => Promise<void>,
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

export const AuthProvider = ({children}: AuthProviderProps) => {
    const navigate = useNavigate();

    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(false);


    const navigateToHomePage = useCallback(async () => {
        navigate("/home")
    }, [navigate])

    const signOut = useCallback(async () => {
        // @Todo: delete cookie
        setUser(null)
        navigate("/")
    }, [navigate])

    const signIn = useCallback(async ({email, password}: SignInProps) => {

        // @Todo: login service

        // @Todo: setar cookie

        // @Todo: setar User
    }, [])

    const signUp = useCallback(
        async ({name, email, password}: SignUpProps) => {

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
