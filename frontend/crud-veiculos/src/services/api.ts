import axios, { AxiosInstance, InternalAxiosRequestConfig } from "axios";


export const API: AxiosInstance = axios.create({
    baseURL: "http://localhost:8080",
    headers: {"Content-Type": "application/json"}
})

API.interceptors.request.use(
    (config: InternalAxiosRequestConfig ): InternalAxiosRequestConfig => {
        const token = localStorage.getItem("accessToken");

        if (config.url?.includes("/auth/")) return config;

        if (token) config.headers.Authorization = `Bearer ${token}`
        return config;
    },
    (error) => Promise.reject(error)
);