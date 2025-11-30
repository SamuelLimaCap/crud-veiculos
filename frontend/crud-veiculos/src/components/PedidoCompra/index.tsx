import { useNavigate } from 'react-router';
import noContent from '../../assets/no-content.jpg'
import { FC, useEffect, useRef, useState } from 'react';
import { API } from '../../services/api';
import { toast } from 'react-toastify';

export default function PedidoCompra(props: any) {

    const navigate = useNavigate()
    const [anuncio, setAnuncio] = useState<any | null>(null)

    const content = props.content;
    const anuncioId = props.content.anuncioVeiculoId;

    const [isDisabled, setIsDisabled] = useState<boolean>(content.disabled);

    const pedidoCompraRef = useRef(null);

    useEffect(() => {
        API.get(`/api/vendas/${anuncioId}`).then(res => (
            setAnuncio(res.data.content)
        )
        )
    }, [])

    function desistirDoPedido() {
        API.delete(`/api/compras/${content.id}`).then(res => {
            toast.success("Pedido deletado com sucesso!")
            content.disabled = true;
        }
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
    return (
        <div className={"anuncio" + (isDisabled ? "background-secondary" : "")} ref={pedidoCompraRef}>
            <div className="image">
                {
                    (anuncio?.imageBase64) ?
                        (
                            <img src={`data:${anuncio?.imageType};base64,${anuncio?.imageBase64}`} alt="Car" width={"400px"} />
                        )
                        :
                        (
                            <img src={noContent} width={"240px"} />
                        )
                }
            </div>
            <div className="marca">{anuncio?.veiculo.marca}</div>
            <div className="modelo">{anuncio?.veiculo.modelo}</div>
            <div className="modelo-ano">
                <div className="ano">
                    <i className="bi bi-calendar"></i>{anuncio?.veiculo.ano}
                </div>
                <div className="km-rodados">
                    <i className="bi bi-speedometer"></i>
                    {content.kmRodados} km
                </div>
            </div>

            <div className="preco">R$ {anuncio ? formatPrice(anuncio?.price) : ""} </div>
            <div className="detalhes-pedido-compra mt-2 border border-solid">
                <p>Informações da solicitação do pedido:</p>
                <p>Nome: {content.fullName}</p>
                <p>Email: {content.email}</p>
                <p>Telefone: {content.telefone}</p>
                <p>Mensagem: {content.mensagem}</p>

            </div>
            {
                (isDisabled) ? <h2 className='text text-danger'>Pedido de compra cancelado</h2> : (
                    <div className="btn-ver-detalhes">
                        <button onClick={() => navigate(`/anuncio/${anuncio?.id}`)}>Ver detalhes do anuncio</button>
                        <button className="btn btn-danger" onClick={desistirDoPedido}>Desistir do pedido</button>
                    </div>
                )
            }
        </div>
    );
}