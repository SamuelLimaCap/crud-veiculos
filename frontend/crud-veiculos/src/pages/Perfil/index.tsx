import { useContext, useEffect, useState } from "react"
import Layout from "../../components/Layout"
import { API } from "../../services/api"
import PedidoCompra from "../../components/PedidoCompra";
import { AuthContext } from "../../contexts/AuthContext";
import Anuncio from "../../components/Anuncio";

export default function Perfil() {

    const { user } = useContext(AuthContext);
    const [compras, setCompras] = useState<any[] | null>(null);
    const [anuncios, setAnuncios] = useState<any[]>([]);

    useEffect(() => {
        console.log(compras)
    }, [compras])

    useEffect(() => {
        API.get("api/compras"
        ).then(res => {
            setCompras(res.data.content)
        })

        API.get(`api/vendas/getByCreatedUser/${user.idUser}`).then(
            res => {
                setAnuncios(res.data.content)
            }
        )
    }, [])

    return (
        <Layout>
            <div className="perfil-section border border-primary rounded-4 ps-5 pe-5 pb-5 pt-3 mb-3">
                    <h2>Perfil</h2>
                <div className="perfil text-start">

                    <h5 className="p-2">nome</h5>
                    <p className="ps-4">{user?.fullname}</p>

                    <h5 className="p-2">Email</h5>
                    <p className="ps-4">{user?.email}</p>
                </div>
            </div>
            <h4>Seus pedidos de compras</h4>
            <div className="d-flex justify-content-center">
                {
                    (compras != null && compras?.length > 0) ?
                        compras.map(compra => (
                            <PedidoCompra content={compra} />
                        )) : (<p className="text text-secondary text-center">Você não fez nenhum pedido de compra</p>)
                }

            </div>

            <h4>Seus anuncios</h4>
            <div className="d-flex flex-wrap justify-content-center">
                {
                    (anuncios != null && anuncios?.length > 0)
                        ? anuncios.map(a => (<Anuncio content={a} canEdit={true} canDelete={true} />))
                        : (<p className="text text-secondary text-center">Você não anunciou nenhum carro</p>)

                }

            </div>
        </Layout>)

}