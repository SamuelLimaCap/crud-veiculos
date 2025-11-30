import React, { JSX, useState } from "react";
import Navbar from "../Navbar";
import './index.css'

interface LayoutProps {
    children: React.ReactNode
}
export default function Layout({ children }: LayoutProps) {
    return <>
        <Navbar />
        <div className="nav-children">

        {children}
        </div>
    </>
}