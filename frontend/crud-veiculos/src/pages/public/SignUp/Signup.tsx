import { useState } from "react";
import { useForm } from "react-hook-form";
import { useNavigate } from "react-router";
import { useAuth } from "../../../hooks/useAuth";
import logo from './../../../assets/logo-2.jpeg'
import './index.css'
import { toast } from "react-toastify";

export default function Signup() {
    const navigate = useNavigate();
    const {signUp, } = useAuth();
    const [loading, setIsLoading] = useState(false);

    const {
        register,
        handleSubmit,
        watch,
        reset,
        formState: { errors, isSubmitting }
    } = useForm();
    const password = watch("password");

    const onSubmit = async (signUpForm: any) => {
        try {
            setIsLoading(true)
            await signUp({
                fullname: signUpForm.fullname,
                email: signUpForm.email,
                password: signUpForm.password,
            })
            toast.success("Cadastrado com sucesso!")
        } catch (error) {
            console.log(error)
        } finally {
            setIsLoading(false)
        } 
    }

    return (
        <div className="container-signup">
            <div className="row">
                <div className="col">
                    <img src={logo} />
                </div>
                <div className="col">
                    <form onSubmit={handleSubmit(onSubmit)} className="form-signup">
                        <div className="mb-3">
                            <label htmlFor="full-name">Nome completo</label>
                            <input type="text" id="full-name" placeholder="Digite seu nome completo"
                                {...register("fullname", { required: "Nome é obrigatório" })}
                                className="form-control"
                            />

                            {errors.username && (<span className="error-input">
                                {errors.username.message}
                            </span>)}
                        </div>

                        <div className="mb-3">
                            <label htmlFor="full-name">Nome completo</label>
                            <input type="email" id="email" placeholder="email@email.com"
                                {...register("email", {
                                    required: "Email é obrigatório",
                                    pattern: {
                                        value: /^[^@ ]+@[^@ ]+\.[^@ .]{2,}$/,
                                        message: "Email inválido",
                                    },
                                })}
                                className="form-control"
                            />

                            {errors.email && (<span className="error-input">
                                {errors.email.message}
                            </span>)}
                        </div>

                        <div className="mb-3">
                            <label htmlFor="signup-password">
                                Senha
                            </label>
                            <input
                                type="password"
                                id="signup-password"
                                placeholder="Mínimo 8 caracteres"
                                className="form-control"
                                {...register("password", {
                                    required: "Senha obrigatória",
                                    minLength: {
                                        value: 8,
                                        message: "A senha deve ter pelo menos 8 caracteres",
                                    },
                                })}
                            />
                            {errors.password && (
                                <span className="error-input">
                                    {errors.password.message}
                                </span>
                            )}
                        </div>

                        <div className="mb-3">
                            <label
                                htmlFor="signup-confirm-password"
                            >
                                Confirmar senha
                            </label>
                            <input
                                type="password"
                                id="signup-confirm-password"
                                placeholder="Repita sua senha"
                                className="form-control d-center text-center"
                                {...register("confirmPassword", {
                                    required: "Confirmação obrigatória",
                                    validate: (value) =>
                                        value === password || "As senhas não coincidem",
                                })}
                            />
                            {errors.confirmPassword && (
                                <span className="error-input">
                                    {errors.confirmPassword.message}
                                </span>
                            )}
                        </div>

                        <div className="input-group d-flex justify-content-center">
                            <button
                                type="submit"
                                id="create-account-button"
                                disabled={isSubmitting}
                            >
                                <i className="fas fa-user-plus"></i>
                                {isSubmitting ? "Criando..." : "Criar conta"}
                            </button>
                        </div>
                    </form>

                </div>
                    <div>Ja tem uma conta? Clique aqui e <a href="#" onClick={() => navigate("/")}>logue-se </a></div>
            </div>
        </div>
    )

}