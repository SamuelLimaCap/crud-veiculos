import { useCallback, useEffect, useState } from "react";
import { Controller, useForm } from "react-hook-form";
import Layout from "../../../components/Layout";
import { API } from "../../../services/api";
import { useParams } from "react-router";
import noContentImg from './../../../assets/no-content.jpg'

export default function EditarAnuncio(props: any) {

    const [anuncio, setAnuncio] = useState<any | null>();
    const { id } = useParams<{ id: string }>();

    const {
        register,
        handleSubmit,
        reset,
        control,
        formState: { errors, isSubmitting }
    } = useForm({
        mode: 'onChange'
    })

    useEffect(() => {
        API.get(`api/vendas/${id}`).then(res => {
            setAnuncio(res.data.content)
        })
    }, [])

    function isStringValid(str?: string) {
        return str && str.trim().length > 0;
    }

    const onSubmit = useCallback(
        async (content: any) => {
            try {
                console.log(content)
                const data = {
                    placa: (isStringValid(content.placa) ? content.placa : ''),
                    kmRodados: content.kmRodados?.replace(",", "."),
                    price: content.price?.replace(",", "."),
                    image: content.image ? content.image : null,
                    cor: (content.cor != "" ? content.cor : '')
                }

                const formData = new FormData();


                formData.append("id", id!);
                formData.append("placa", data.placa)
                formData.append("cor", data.cor)
                formData.append("kmRodados", data.kmRodados)

                if (data.image && data.image.length > 0) {
                    formData.append('image', data.image[0]);
                }

                const response = await API.patch(
                    '/api/vendas',
                    formData,
                    {
                        headers: {
                            'Content-Type': 'multipart/form-data',
                        },
                    }
                );

                reset();
            } catch (error) {
                console.log(error)
            } finally {
                // setLoading(false)
            }
        }, []
    )

    const handlePriceKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
        const key = e.key;

        // Allow: digits (0-9), comma, backspace, delete, arrow keys, tab
        const isDigit = /\d/.test(key);
        const isComma = key === ',';
        const isControlKey = ['Backspace', 'Delete', 'ArrowLeft', 'ArrowRight', 'Tab'].includes(key);

        if (!isDigit && !isComma && !isControlKey) {
            e.preventDefault();
            return;
        }
        // Prevent multiple commas
        if (key === ',' && e.target.value.includes(',')) {
            e.preventDefault();
        }
    };
    return (
        <Layout>
            <div>
                <div className="row space-around vh-100 vw-100">
                    <div className="col-6 conteudo ">
                        <div className="row img">
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
                            <div className="col">
                                <h6>Placa</h6>
                                {anuncio?.placa}
                            </div>
                        </div>
                    </div>
                    <div className="col-2">
                        <form onSubmit={handleSubmit(onSubmit)} className="">
                            <div className="mb-3">
                                <label htmlFor="placa" className="form-label">Placa do carro</label>
                                <input type="text" className={"form-control " + (errors.placa ? " input-error" : "")} placeholder="formato CCC1234 ou CCC1C23"
                                    {...register("placa", {
                                        pattern: {
                                            value: /^[A-Z]{3}-?\d{4}|[A-Z]{3}\d{1}[A-Z]{1}\d{1}/,
                                            message: "Placa não existente no mercosul"
                                        },
                                    })}
                                />
                                {errors.placa && (
                                    <span className="formError">{errors.placa.message}</span>
                                )}
                            </div>
                            <div className="mb-3">
                                <label htmlFor="cor" className="form-label">Cor do carro</label>
                                <input type="text" className={"form-control"}
                                    {...register("cor", {
                                    })}

                                />
                            </div>
                            <div className="mb-3">
                                <label htmlFor="kmRodados" className="form-label">Km rodados</label>
                                <Controller
                                    control={control}
                                    name="kmRodados"
                                    rules={{
                                        pattern: {
                                            value: /^\d+(\,\d{3})?$/,
                                            message: "Formato valido: 100,000"
                                        }
                                    }}
                                    render={({ field }) => (
                                        <input
                                            {...field}
                                            type="text"
                                            id="km-rodados"
                                            className={"form-control" + (errors.kmRodados ? " input-error" : "")}
                                            placeholder="10,00"
                                            onKeyDown={handlePriceKeyDown}
                                        />
                                    )}
                                />

                                {errors.kmRodados && (
                                    <span className="formError">{errors.kmRodados.message}</span>
                                )}
                            </div>
                            <div className="mb-3">
                                <label htmlFor="price" className="form-label">Preço</label>
                                <Controller
                                    control={control}
                                    name="price"
                                    rules={{
                                        pattern: {
                                            value: /^\d+(\,\d{2})?$/,
                                            message: "Formato valido: 10,00"
                                        }
                                    }}
                                    render={({ field }) => (
                                        <input
                                            {...field}
                                            type="text"
                                            id="price"
                                            className={"form-control" + (errors.price ? " input-error" : "")}
                                            placeholder="10,00"
                                            onKeyDown={handlePriceKeyDown}
                                        />
                                    )}
                                />

                                {errors.price && (
                                    <span className="formError">{errors.price.message}</span>
                                )}
                            </div>
                            <div className="mb-3">
                                <label htmlFor="image">Nova Imagem do Veículo:</label>
                                <Controller
                                    name="image"
                                    control={control}
                                    rules={{}}
                                    render={({ field: { value, onChange, ...field } }) => (
                                        <input
                                            {...field}
                                            id="image"
                                            type="file"
                                            className="form-control"
                                            accept="image/*"
                                            onChange={(e) => onChange(e.target.files)}
                                        />
                                    )}
                                />
                                {errors.image && <small style={{ color: 'red' }}>{errors.image.message}</small>}
                            </div>

                            <input type="submit" value="Editar anuncio" />
                        </form>
                    </div>
                </div>
            </div>
        </Layout>
    )
}