import { JSX, useEffect, useState } from 'react';
import Navbar from '../../../components/Navbar';
import './index.css'
import Layout from '../../../components/Layout';
import Anuncio from '../../../components/Anuncio';
import { API } from '../../../services/api';

enum TipoFiltro {
    ORDENAR,
    PRECO,
    KM,
    ANO,
    MARCA,
    MODELO,
}
export default function Lista() {

    const [filtroAtivo, setFiltroAtivo] = useState(false);
    const [tipoFiltro, setTipoFiltro] = useState<TipoFiltro | null>(null);
    const [conteudoFiltro, setConteudoFiltro] = useState<JSX.Element | null>(null);
    const [listaAnuncios, setListaAnuncios] = useState<any[]>([]);

    useEffect(() => {
        API.get("api/vendas").then(
            res => {
                setListaAnuncios(res.data.content)
                console.log(res.data.content)
            }
        )
    }, [])
    const filtroOrdenar = () => <div>

        <p><input type="radio" name="filtro-ordenar" value="menor-preco" /><label>Menor preço</label></p>
        <p><input type="radio" name="filtro-ordenar" value="menor-km" /><label>Menor km</label></p>
        <p><input type="radio" name="filtro-ordenar" value="menor-ano" /><label>Mais antigo</label></p>
        <p><input type="radio" name="filtro-ordenar" value="maior-preco" /><label>Maior preço</label></p>
        <p><input type="radio" name="filtro-ordenar" value="maior-km" /><label>Maior km</label></p>
        <p><input type="radio" name="filtro-ordenar" value="maior-ano" /><label>Mais novo</label></p>
    </div>

    const filtroPreco = () => <div className='d-flex justify-content-center p-5'>
        <div className="row me-5">
            <label htmlFor="preco-min" className=''>Preço min</label>
            <input type="number" name="preco-min" id="" />
        </div>
        <div className="row">
            <label htmlFor="preco-max">Preço max</label>
            <input type="number" name="preco-max" id="" />
        </div>
    </div>

    const filtroKm = () => <div className='d-flex justify-content-center p-5'>
        <div className="row me-5">
            <label htmlFor="km-min" className=''>Km min</label>
            <input type="number" name="km-min" id="" />
        </div>
        <div className="row">
            <label htmlFor="km-max">Km max</label>
            <input type="number" name="km-max" id="" />
        </div>
    </div>

    const filtroAno = () => <div className='d-flex justify-content-center p-5'>
        <div className="row me-5">
            <label htmlFor="km-min" className=''>Ano min</label>
            <input type="number" name="km-min" id="" />
        </div>
        <div className="row">
            <label htmlFor="km-max">Ano max</label>
            <input type="number" name="km-max" id="" />
        </div>
    </div>

    const filtroModelo = () => <div className='d-flex justify-content-center p-5'>
        <div className="row me-5">
            <label htmlFor="km-min" className=''>Modelo</label>
            <input type="text" name="modelo" id="" />
        </div>
    </div>

    const filtroMarca = () => <div className='d-flex justify-content-center p-5'>
        <div className="row me-5">
            <label htmlFor="km-min" className=''>Marca</label>
            <input type="text" name="marca" id="" />
        </div>
    </div>

    function selectTipoFiltro(tipoFiltro: TipoFiltro) {
        setFiltroAtivo(true)
        setTipoFiltro(tipoFiltro)
    }

    function closeFiltroAtivo() {
        setFiltroAtivo(!filtroAtivo)
        setTipoFiltro(null)
        setConteudoFiltro(null)
    }

    useEffect(() => {
        switch (tipoFiltro) {
            case null: setConteudoFiltro(null); return;
            case TipoFiltro.ORDENAR: setConteudoFiltro(filtroOrdenar); return;
            case TipoFiltro.PRECO: setConteudoFiltro(filtroPreco); return;
            case TipoFiltro.KM: setConteudoFiltro(filtroKm); return;
            case TipoFiltro.ANO: setConteudoFiltro(filtroAno); return;
            case TipoFiltro.MODELO: setConteudoFiltro(filtroModelo); return;
            case TipoFiltro.MARCA: setConteudoFiltro(filtroMarca); return;
            default: setConteudoFiltro(null)
        }
    }, [tipoFiltro])

    return <>
        <Layout>
        <main>
            <div className="tela-principal">
                <div className="menu-filtro">
                    <button className='btn-filtro' onClick={() => selectTipoFiltro(TipoFiltro.ORDENAR)}>Ordenar por</button>
                    <button className='btn-filtro' onClick={() => selectTipoFiltro(TipoFiltro.PRECO)}>Preço</button>
                    <button className='btn-filtro' onClick={() => selectTipoFiltro(TipoFiltro.KM)}>Quilometragem</button>
                    <button className='btn-filtro' onClick={() => selectTipoFiltro(TipoFiltro.ANO)}>Ano</button>
                    <button className='btn-filtro' onClick={() => selectTipoFiltro(TipoFiltro.MARCA)}>Marca</button>
                    <button className='btn-filtro' onClick={() => selectTipoFiltro(TipoFiltro.MODELO)}>Modelo</button>
                </div>

                <div className="container-duplo">
                    <div className="filtro-busca">
                        {
                            (filtroAtivo ?
                                <div className='conteudo-filtro container-sm mt-3'>
                                    <div className="close-btn-filtro">
                                        <a href="#" className='w-100' onClick={closeFiltroAtivo}><i className="bi bi-x-square"></i></a>

                                    </div>
                                    {conteudoFiltro}</div>
                                : null)
                        }
                    </div>
                    <div className="conteudo">
                        {
                            listaAnuncios.map((c, i) => (
                                <Anuncio content={c} key={i} />
                            ))
                        }


                    </div>
                </div>
            </div>

        </main>
    </Layout >

    </>

}