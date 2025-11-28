import { useAuth } from "../../../hooks/useAuth";
import { useCallback, useState } from "react";
import './index.css'
import { useNavigate } from "react-router";
import { useForm } from "react-hook-form";

export default function Login() {
    const { signIn } = useAuth()
    const navigate = useNavigate()

    const {
        register,
        handleSubmit,
        reset,
        formState: { errors, isSubmitting }
    } = useForm()

    const [loading, setLoading] = useState(false);
    const [showPassword, setShowPassword] = useState(false);


    const onSubmit = useCallback(
        async (content: any) => {
            console.log(content)
            try {
                await signIn({
                    email: content.email,
                    password: content.password
                })
            } catch (error) {
                console.log(error)
            } finally {
                setLoading(false)
            }
        }, [signIn]
    )

    const passwordEyeOpen =
        <i className="bi bi-eye-fill" aria-hidden="true"></i>

    const passwordEyeClosed =
        <i className="bi bi-eye-slash-fill" aria-hidden="true"></i>


    const changePassword = () => {
        setShowPassword(!showPassword)
    }

    const redirectToSignUp = () => {
        navigate("signup/")
    }

    return <>
        <div className={"container container-login"}>
            <div className={"row"}>
                <div className={"col"}>
                    Aqui vai estar o anuncio
                </div>
                <div className={"col"}>

                    <form onSubmit={handleSubmit(onSubmit)} className="form-login">
                        <div className="mb-3">
                            <label htmlFor="form-email" className="form-label">Email</label>
                            <div className="input-group">
                                <input type="email" className="form-control" id="form-email" placeholder="email@email.com"
                                    {...register("email", {
                                        required: "E-mail é obrigatório",
                                        pattern: {
                                            value: /^[^@ ]+@[^@ ]+\.[^@ .]{2,}$/,
                                            message: "E-mail inválido",
                                        }
                                    })}
                                />
                                <div className="input-group-post-email">
                                    <span>@</span>
                                </div>
                            </div>
                            {errors.email && (
                                <span className="formError">{errors.email.message}</span>
                            )}
                        </div>

                        <div className="mb-3">
                            <label htmlFor="form-password" className="form-label">password</label>
                            <div className="input-group">
                                <input type={(showPassword) ? "text" : "password"} className="form-control" id="form-password" placeholder="senha"
                                    {...register("password", { required: "Senha obrigatória" })}
                                />
                                <div className="input-group-show-password d-flex text-center">
                                    <a href="#" onClick={changePassword} className="d-flex align-itens-center justify-content-center">
                                        {(showPassword) ? passwordEyeOpen : passwordEyeClosed}
                                    </a>
                                </div>
                            </div>

                            {errors.password && (
                                <span className={"formError"}>
                                    {errors.password.message}
                                </span>
                            )}

                            <input type="submit" value="logar" />

                        </div>
                    </form>

                    <div className="divider" />

                    <div>Ainda não tem uma conta? Clique aqui e <a href="#" onClick={redirectToSignUp}>cadastre-se </a></div>
                </div>
            </div>
        </div>
    </>
}