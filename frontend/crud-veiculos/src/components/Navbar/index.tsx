import './index.css'
import logo from './../../assets/logo-2.jpeg';
import { useContext, useRef, useState } from 'react';
import { AuthContext } from '../../contexts/AuthContext';
import { Link } from 'react-router';

export default function Navbar() {

    const primaryNavRef = useRef(null);
    const navToggleRef = useRef(null);
    const [isVisible, setIsVisible] = useState(false);

    const { signOut } = useContext(AuthContext);


    function toggleNavVisible(e: React.MouseEvent<HTMLButtonElement>) {
        setIsVisible(!isVisible)
    }

    function logout() {
        signOut();
    }

    return <header>
            <div className="logo">
                <a href="#home">
                    <img src={logo} alt="" />
                </a>
            </div>
            <button
                className="mobile-nav-toggle"
                ref={navToggleRef}
                aria-controls="primary-navigation"
                onClick={toggleNavVisible}
            ></button>
            <nav className="primary-navigation" ref={primaryNavRef} data-visible={isVisible}>
                <li>
                    <Link to={"/home"}>Comprar</Link>
                </li>
                <li>
                    <Link to={"/anunciar"}>Anunciar</Link>
                </li>
                <li>
                    <Link to={"/perfil"}>Perfil</Link>
                </li>
                <li>
                    <a href="#" onClick={logout}>Logout</a>
                </li>
            </nav>
        </header>
}