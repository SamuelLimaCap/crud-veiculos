import { useContext, useEffect, useState } from "react";
import noContentImg from './../../../assets/no-content.jpg'
import './index.css'
import Layout from "../../../components/Layout";
import { useNavigate, useParams } from "react-router";
import { API } from "../../../services/api";
import { useForm } from "react-hook-form";
import { AuthContext } from "../../../contexts/AuthContext";
import { toast } from "react-toastify";

interface AnuncioByIdProps {
    id: String,
}

interface AnuncioProps {
    userId: string
    placa: string,
    preco: number,
    modea: string,
    estado: string,
    kmRodados: number,
    veiculo: VeiculoProps,
    imageBase64: any,
    imageName: string,
    imageType: string,
}

interface VeiculoProps {
    tipo: string,
    marca: string,
    modelo: string,
    ano: string,
    cor: string,
    combustivel: string,
}

export default function AnuncioByID({ }: AnuncioByIdProps) {

    const [anuncio, setAnuncio] = useState<AnuncioProps | null>(null);
    const [pedidosCompra, setPedidosCompra] = useState<any[]>([]);
    const { id } = useParams<{ id: string }>();
    const { user } = useContext(AuthContext);
    const navigate = useNavigate();

    const {
        register, handleSubmit, reset,
        formState: { errors, isSubmitting }
    } = useForm();

    const onSubmit = (data: any) => {
        API.post("/api/compras", {
            "idAnuncio": id,
            "nome": data.nome,
            "email": data.email,
            "telefone": data.telefone,
            "mensagem": data.mensagem
        }).then(res => {
            toast.success("mensagem enviada!")
            navigate("/home")
        }
        )
    }

    function onFinalizarComCliente(idCliente: string) {
        API.patch(`/api/vendas/${id}/cliente/${idCliente}`).then(res => {
            toast.success("Anuncio finalizado com esse cliente")
            navigate("/home")
        })

    }

    useEffect(() => {
        API.get(`api/vendas/${id}`).then(res => (
            setAnuncio(res.data.content))
        )
    }, [id])

    useEffect(() => {
        if (anuncio?.userId == user?.idUser) {
            API.get(`/api/compras/anuncio/${id}`).then(
                res => (setPedidosCompra(res.data.content))
            )
        }

    }, [anuncio])

    return (
        <Layout>
            <div className="row space-around vh-100 vw-100">
                <div className="col-6 conteudo ">
                    <div className="row w-100 img">
                        {
                            (anuncio?.imageBase64) ?
                                (
                                    <img src={`data:${anuncio.imageType};base64,${anuncio.imageBase64}`} alt="Car" width={"400px"} />
                                )
                                :
                                (
                                    <img src={noContentImg} width={"240px"} />
                                )
                        }
                    </div>
                    <div className="row">
                        <div className="col">
                            <h6>Ano</h6>
                            {anuncio?.veiculo?.ano}
                        </div>
                        <div className="col">
                            <h6>KM</h6>
                            {anuncio?.kmRodados}
                        </div>
                        <div className="col">
                            <h6>Combustivel</h6>
                            {anuncio?.veiculo?.combustivel}
                        </div>
                        <div className="col">
                            <h6>Cor</h6>
                            {anuncio?.veiculo?.cor}

                        </div>
                    </div>
                    <div className="row">
                        <div className="col">
                            <h6>Placa</h6>
                            {anuncio?.placa}
                        </div>
                    </div>
                </div>
                {
                    (anuncio?.estado == "ENCERRADO") ? (
                        <div className="col-2">
                            <h2>Anuncio encerrado</h2>
                        </div>
                    ) :
                        (anuncio?.userId != user?.idUser) ?
                            (
                                <div className="col-2 contato">
                                    <h2>{anuncio?.preco}</h2>
                                    <form onSubmit={handleSubmit(onSubmit)}>

                                        Fale com o vendedor:
                                        <p>
                                            <input type="text" placeholder="nome completo*"
                                                {...register("nome", {
                                                    required: "Preencha seu nome"
                                                })}
                                            />
                                        </p>
                                        <p>
                                            <input type="email" placeholder="email*"
                                                {...register("email", {
                                                    required: "Preencha seu email"
                                                })}
                                            />
                                        </p>
                                        <p>
                                            <input type="text" placeholder="telefone*"

                                                {...register("telefone", {
                                                    required: "Preencha seu telefone"
                                                })}
                                            />
                                        </p>
                                        <p>
                                            <input type="textarea" placeholder="mensagem*"

                                                {...register("mensagem", {
                                                    required: "Mensagem obrigatória"
                                                })}
                                            />
                                        </p>

                                        <button type="submit">Enviar mensagem</button>
                                    </form>
                                </div>
                            ) : (
                                <div className="col-2 pedidos-compra">
                                    <h4>Pedidos de compra</h4>
                                    <span className="text text-secondary">(Obs: Chame e converse com o cliente fora do sistema. Caso feche o negócio com ele, marque como finalizado selecionando a mensagem do cliente)</span>
                                    {pedidosCompra?.map(pedido => (
                                        <div className="border border-solid rounded-3 p-2">
                                            <p>
                                                Email: {pedido.email}
                                            </p>
                                            <p>
                                                Nome: {pedido.fullName}
                                            </p>
                                            <p>
                                                telefone: {pedido.telefone}
                                            </p>

                                            <p>
                                                Mensagem: {pedido.mensagem}
                                            </p>

                                            <div className="buttons ">
                                                <button onClick={() => onFinalizarComCliente(pedido.userIdDoPedido)} className="btn btn-outline-success">Finalizei com esse cliente</button>
                                            </div>
                                        </div>

                                    ))}
                                </div>
                            )
                }
            </div>
        </Layout>
    )

}