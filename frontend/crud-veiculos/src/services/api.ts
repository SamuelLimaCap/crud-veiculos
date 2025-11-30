import axios, { AxiosInstance, InternalAxiosRequestConfig } from "axios";
import { toast } from "react-toastify";


export const API: AxiosInstance = axios.create({
    baseURL: "http://localhost:8080",
    headers: { "Content-Type": "application/json" }
})

API.interceptors.request.use(
    (config: InternalAxiosRequestConfig): InternalAxiosRequestConfig => {
        const token = localStorage.getItem("accessToken");

        if (config.url?.includes("/auth/")) return config;

        if (token) config.headers.Authorization = `Bearer ${token}`
        return config;
    },
    (error) => Promise.reject(error)
);

API.interceptors.response.use(
    (response) => response,
    async (error) => {
        const original = error.config;
        const status = error.response?.status;

        if (
            status !== 401 ||
            original.url.includes("/auth/refresh-token") ||
            original.url.includes("/auth/login")
        ) {
            toast.error(error.response?.data?.message)
            return Promise.reject(error);
        }

        if (status == 401) {
            toast.error("Acesso expirado, acesse sua conta novamente")
            localStorage.setItem("accessToken", '')
            window.location.href = "/"
        }
            return Promise.reject(error);
    }
)