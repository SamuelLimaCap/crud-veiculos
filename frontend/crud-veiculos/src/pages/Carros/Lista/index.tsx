import { JSX, useEffect, useReducer, useState } from 'react';
import Navbar from '../../../components/Navbar';
import './index.css'
import Layout from '../../../components/Layout';
import Anuncio from '../../../components/Anuncio';
import { API } from '../../../services/api';
import FiltroOrdenar from '../../FiltrosBusca/FiltroOrdenar';
import FiltroPreco from '../../FiltrosBusca/FiltroPreco';
import FiltroKm from '../../FiltrosBusca/FiltroKm';
import FiltroAno from '../../FiltrosBusca/FiltroAno';
import FiltroModelo from '../../FiltrosBusca/FiltroModelo';
import FiltroMarca from '../../FiltrosBusca/FiltroMarca';

enum TipoFiltro {
    ORDENAR,
    PRECO,
    KM,
    ANO,
    MARCA,
    MODELO,
}

interface PageInfo {
    pageNum: number;
    size: number;
    totalElements: number;
    totalPages: number;
}

interface ApiResponse {
    status: string;
    message: string;
    content: any[];
    pageInfo: PageInfo;
}

interface State {
    anuncios: any[];
    pageInfo: PageInfo;
    loading: boolean;
    error: string | null;
}

type Action =
    | { type: 'FETCH_START' }
    | { type: 'FETCH_SUCCESS'; payload: ApiResponse }
    | { type: 'APPEND_SUCCESS'; payload: ApiResponse }
    | { type: 'FETCH_ERROR'; payload: string };

const initialState: State = {
    anuncios: [],
    pageInfo: {
        pageNum: 0,
        size: 10,
        totalElements: 0,
        totalPages: 0,
    },
    loading: false,
    error: null,
};

const reducer = (state: State, action: Action): State => {
    switch (action.type) {
        case 'FETCH_START':
            return { ...state, loading: true, error: null };

        case 'FETCH_SUCCESS':
            return {
                ...state,
                anuncios: action.payload.content,
                pageInfo: action.payload.pageInfo,
                loading: false,
            };

        case 'APPEND_SUCCESS':
            return {
                ...state,
                anuncios: [...state.anuncios, ...action.payload.content],
                pageInfo: action.payload.pageInfo,
                loading: false,
            };

        case 'FETCH_ERROR':
            return { ...state, error: action.payload, loading: false };

        default:
            return state;
    }
};

export default function Lista() {
    const [state, dispatch] = useReducer(reducer, initialState);
    const [currentPage, setCurrentPage] = useState(0);
    const [filtroAtivo, setFiltroAtivo] = useState(false);
    const [tipoFiltro, setTipoFiltro] = useState<TipoFiltro | null>(null);
    const [conteudoFiltro, setConteudoFiltro] = useState<JSX.Element | null>(null);
    const [filterParams, setFilterParams] = useState<any>([]);

    const fetchAnuncios = async (pageNum: number = 0) => {
        dispatch({ type: 'FETCH_START' });

        try {
            const response = await API.get('api/vendas/filtrar', {
                params: { page: pageNum, size: 2, ...filterParams },
            });

            if (pageNum === 0) {
                dispatch({ type: 'FETCH_SUCCESS', payload: response.data });
            } else {
                dispatch({ type: 'APPEND_SUCCESS', payload: response.data });
            }

            setCurrentPage(pageNum);
        } catch (error: any) {
            dispatch({
                type: 'FETCH_ERROR',
                payload: error.message || 'Erro ao carregar anúncios',
            });
        }
    };

    useEffect(() => {
        fetchAnuncios(0);
    }, []);

    useEffect(() => {
        fetchAnuncios(0);
    }, [filterParams]);
    const handleShowMore = () => {
        const nextPage = currentPage + 1;
        fetchAnuncios(nextPage);
    };

    const shouldShowMore = state.anuncios.length < state.pageInfo.totalElements;

    function selectTipoFiltro(tipoFiltro: TipoFiltro) {
        setFiltroAtivo(true)
        setTipoFiltro(tipoFiltro)
    }

    function closeFiltroAtivo() {
        setFiltroAtivo(!filtroAtivo)
        setTipoFiltro(null)
        setConteudoFiltro(null)
    }
    const handleApplyFilter = async (filterParams: any) => {
        setFilterParams(filterParams)
    };

    const handleOnSelectOrdenar = (value: string) => {
        handleApplyFilter({ordenarPor: value })
    }

    const handleOnFilterPreco = (min: number, max: number) => {
        handleApplyFilter({precoMin: min, precoMax: max})
    }

    const handleOnFilterKm = (min: number, max: number) => {
        handleApplyFilter({kmMin: min, kmMax: max})
    }

    const handleOnFilterAno = (min: number, max: number) => {
        handleApplyFilter({anoMin: min, anoMax: max})
    }

    const handleOnSelectModelo = (modelo: string) => {
        handleApplyFilter({modelo: modelo})
    }

    const handleOnSelectMarca = (marca: string) => {
        handleApplyFilter({marca: marca})
    }
    useEffect(() => {
        switch (tipoFiltro) {
            case null: setConteudoFiltro(null); return;
            case TipoFiltro.ORDENAR: setConteudoFiltro(<FiltroOrdenar onSelect={handleOnSelectOrdenar}/>); return;
            case TipoFiltro.PRECO: setConteudoFiltro(<FiltroPreco onFilter={handleOnFilterPreco}/>); return;
            case TipoFiltro.KM: setConteudoFiltro(<FiltroKm onFilter={handleOnFilterKm}/>); return;
            case TipoFiltro.ANO: setConteudoFiltro(<FiltroAno onFilter={handleOnFilterAno}/>); return;
            case TipoFiltro.MODELO: setConteudoFiltro(<FiltroModelo onFilter={handleOnSelectModelo}/>); return;
            case TipoFiltro.MARCA: setConteudoFiltro(<FiltroMarca onFilter={handleOnSelectMarca}/>); return;
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
                                        {conteudoFiltro}
                                    </div>
                                    : null)
                            }
                        </div>
                        <div className="conteudo">
                            {state.anuncios.length === 0 && !state.loading && (
                                <p>Nenhum anúncio encontrado</p>
                            )}

                            {state.anuncios.map((c, i) => (
                                <Anuncio content={c} key={i} />
                            ))}

                            {state.error && (
                                <p style={{ color: 'red' }}>{state.error}</p>
                            )}

                            {state.loading && (
                                <p>Carregando...</p>
                            )}

                            {shouldShowMore && (
                                <div style={{ textAlign: 'center', marginTop: '20px' }}>
                                    <button
                                        onClick={handleShowMore}
                                        disabled={state.loading}
                                        style={{
                                            padding: '10px 30px',
                                            backgroundColor: '#007bff',
                                            color: 'white',
                                            border: 'none',
                                            borderRadius: '4px',
                                            cursor: state.loading ? 'not-allowed' : 'pointer',
                                            opacity: state.loading ? 0.6 : 1,
                                            fontSize: '16px',
                                        }}
                                    >
                                        {state.loading ? 'Carregando...' : 'Mostrar Mais'}
                                    </button>
                                    <p style={{ fontSize: '12px', color: '#666', marginTop: '10px' }}>
                                        Exibindo {state.anuncios.length} de {state.pageInfo.totalElements} anúncios
                                    </p>
                                </div>
                            )}
                        </div>
                    </div>
                </div>

            </main>
        </Layout >

    </>

}