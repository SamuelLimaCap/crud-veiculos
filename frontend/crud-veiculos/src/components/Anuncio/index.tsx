
import { useNavigate } from 'react-router'
import noContent from '../../assets/no-content.jpg'
import './index.css'
import { FC, useRef } from 'react';
import { API } from '../../services/api';

export default function Anuncio(props: any) {
    const navigate = useNavigate()

    const content = props.content;

    const anuncioRef = useRef(null);

    function deletarAnuncio(id: string) {
        API.delete(`/api/vendas/invalidar-anuncio/${id}`).then(
            anuncioRef.current.style.display = 'none'
        )
    }

    const PriceDisplay: FC<any> = ({ value }) => {
        const formatted = new Intl.NumberFormat('pt-BR', {
            style: 'currency',
            currency: 'BRL',
        }).format(value);

        return <span>{formatted}</span>;
    };

    const formatPrice = (value: number): string => {
        return (value).toLocaleString('pt-BR', {
            minimumFractionDigits: 2,
            maximumFractionDigits: 2,
        });
    };

    return <div className="anuncio" ref={anuncioRef}>
        <div className="image">
            {
                (content.imageBase64) ?
                    (
                        <img src={`data:${content.imageType};base64,${content.imageBase64}`} alt="Car" width={"400px"} />
                    )
                    :
                    (
                        <img src={noContent} width={"240px"} />
                    )
            }
        </div>
        <div className="marca">{content.veiculo.marca}</div>
        <div className="modelo">{content.veiculo.modelo}</div>
        <div className="modelo-ano">
            <div className="ano">
                <i className="bi bi-calendar"></i>{content.veiculo.ano}
            </div>
            <div className="km-rodados">
                <i className="bi bi-speedometer"></i>
                {content.kmRodados} km
            </div>
        </div>

        <div className="preco">R$ {formatPrice(content.price)} </div>
        <div className="btn-ver-detalhes">
            <button onClick={() => navigate(`/anuncio/${content.id}`)}>Ver detalhes</button>
            {
                props.canEdit ? (
                    <button className='btn btn-warning' onClick={() => navigate(`/anuncio/editar/${content.id}`)}>Editar</button>
                ) : ""
            }
            {
                props.canDelete ? (
                    <button className='btn btn-danger' onClick={() => deletarAnuncio(content.id)}>Deletar</button>
                ) : ""
            }

        </div>
    </div>
}