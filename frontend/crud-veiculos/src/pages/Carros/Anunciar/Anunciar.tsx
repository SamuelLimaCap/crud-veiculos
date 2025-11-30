import { useCallback, useEffect, useRef, useState } from "react";
import Layout from "../../../components/Layout";
import { COLORS, MARCA_CARROS } from "../../../utils/constants";
import Select, { StylesConfig } from "react-select";
import { API } from "../../../services/api";
import { Controller, useForm } from "react-hook-form";


export default function Anunciar() {

    const [marcas, setMarcas] = useState<any>([]);
    const [selectedMarca, setSelectedMarca] = useState<any>(null);

    const [modelos, setModelos] = useState<any[]>([]);
    const [selectedModelo, setSelectedModelo] = useState<any>(null);

    const [carrosAnosList, setCarrosAnosList] = useState<any[]>([]);
    const [selectedAnoCombustivel, setSelectedAnoCombustivel] = useState<any>(null);
    const [selectAnoDisabled, setSelectAnoDisabled] = useState<boolean>(true);
    const [selectModeloDisabled, setSelectModeloDisabled] = useState<boolean>(true);

    const refSelect = useRef<Select>(null);

    const {
        register,
        handleSubmit,
        reset,
        control,
        formState: { errors, isSubmitting }
    } = useForm({
        mode: 'onChange'
    })

    const handleChangeSelect = (option: any) => {
        console.log(option)
        setSelectedAnoCombustivel(option);
    };

    const onSubmit = useCallback(
        async (content: any) => {
            try {
                const anoCombustivel = selectedAnoCombustivel.label.split(" ");
                console.log(anoCombustivel);
                const data = {
                    placa: content.placa,
                    marca: selectedMarca.label,
                    modelo: selectedModelo.label,
                    combustivel: anoCombustivel[1],
                    ano: anoCombustivel[0],
                    kmRodados: content.kmRodados.replace(",", "."),
                    price: content.price.replace(",", "."),
                    image: content.image,
                    cor: content.cor
                }

                const formData = new FormData();

                formData.append("placa", data.placa)
                formData.append("marca", data.marca)
                formData.append("modelo", data.modelo)
                formData.append("combustivel", data.combustivel)
                formData.append("ano", data.ano)
                formData.append("price", data.price)
                formData.append("image", data.image)
                formData.append("cor", data.cor)
                formData.append("kmRodados", data.kmRodados)

                if (data.image && data.image.length > 0) {
                    console.log(data.image)
                    formData.append('image', data.image[0]);
                }

                const response = await API.post(
                    '/api/vendas/anunciar',
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

    useEffect(() => { //@TODO change on selectedOption

        if (selectedMarca != null) {
            setModelos([])
            setCarrosAnosList([])
            setSelectAnoDisabled(true)
            setSelectModeloDisabled(true)
            API.get(`/api/fipe/marca/${selectedMarca.value}/modelos`).then(res => {
                setModelos(res.data.content)
                setSelectModeloDisabled(false)
            })
        }
    }, [selectedMarca])

    useEffect(() => {
        if (selectedModelo != null) {
            API.get(`/api/fipe/marca/${selectedMarca.value}/modelo/${selectedModelo.value}/anos`).then(res => {
                setCarrosAnosList(res.data.content);
                setSelectAnoDisabled(false)
                setSelectedAnoCombustivel(null);
            })
        }
    }, [selectedModelo])

    useEffect(() => {
        console.log(selectedAnoCombustivel)
        if (selectedAnoCombustivel == null) {
            if (refSelect.current) {
                refSelect.current.setValue({value: null, label: "Selecione..."});
            }
        }
    }, [selectedAnoCombustivel])

    const colourStyles: StylesConfig<any> = {
        control: (styles) => ({ ...styles, backgroundColor: 'white' }),
        option: (styles, { data, isDisabled, isFocused, isSelected }) => {
            return {
                ...styles,
                backgroundColor: isDisabled
                    ? undefined
                    : isSelected
                        ? COLORS[0].borderColor
                        : isFocused
                            ? COLORS[0].primaryColor
                            : COLORS[0].navbarBackgroundColor,
                color: isDisabled
                    ? '#ccc'
                    : isSelected
                        ? COLORS[0].primaryColor
                        : COLORS[0].borderColor,
                cursor: isDisabled ? 'not-allowed' : 'default',

                ':active': {
                    ...styles[':active'],
                    backgroundColor: !isDisabled
                        ? isSelected
                            ? "black"
                            : "white"
                        : undefined,
                },
            };
        },
        input: (styles) => ({ ...styles, }),
        placeholder: (styles) => ({ ...styles, }),
        singleValue: (styles, { data }) => ({ ...styles, }),
    };

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

    useEffect(() => {
        if (marcas != null) {
            setMarcas(MARCA_CARROS.map(marca => ({ value: marca.codigo, label: marca.nome })))
        }
    }, [])
    return (
        <Layout>
            <div>
                <form onSubmit={handleSubmit(onSubmit)} className="">
                    <div className="mb-3">
                        <label htmlFor="placa" className="form-label">Placa do carro</label>
                        <input type="text" className={"form-control " + (errors.placa ? " input-error" : "")} placeholder="formato CCC1234 ou CCC1C23"
                            {...register("placa", {
                                required: "Placa é obrigatória",
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
                        <label htmlFor="marca" className="form-label">Marca</label>
                        <Select className="marca-select" classNamePrefix={"select"}
                            options={[
                                ...marcas
                            ]}
                            onChange={setSelectedMarca}
                            defaultValue={selectedMarca}
                            styles={colourStyles}
                            placeholder="Selecione..."

                        />
                    </div>
                    <div className="mb-3">
                        <label htmlFor="modelo" className="form-label">Modelo</label>
                        <Select className="modelo-select" classNamePrefix={"select"}
                            options={[
                                ...modelos.map(modelo => ({ value: modelo.codigo, label: modelo.modelo }))
                            ]}
                            onChange={setSelectedModelo}
                            defaultValue={selectedModelo}
                            styles={colourStyles}
                            placeholder={selectModeloDisabled ? "Selecione a marca" : "Selecione..."}
                            isDisabled={selectModeloDisabled}
                            isClearable={true}
                            isSearchable={true}

                        />
                    </div>
                    <div className="mb-3">
                        <label htmlFor="ano" className="form-label">Ano e combustivel</label>
                        <Select className="ano-select" classNamePrefix={"select"} ref={refSelect}
                            options={[
                                {value: null, label: 'Selecione...', isDisabled: true},
                                ...carrosAnosList.map(ano => ({ value: ano.codigo, label: ano.nome }))
                            ]}
                            onChange={handleChangeSelect}
                            defaultValue={selectedAnoCombustivel}
                            styles={colourStyles}
                            placeholder={selectAnoDisabled ? "Selecione o modelo" : "Selecione..."}
                            isDisabled={selectAnoDisabled}
                            isClearable={true}
                            isSearchable={true}
                        />
                    </div>
                    <div className="mb-3">
                        <label htmlFor="cor" className="form-label">Cor do carro</label>
                        <input type="text" className={"form-control"}
                            {...register("cor", {
                                required: "Não pode estar vazio"
                            })}

                        />
                    </div>
                    <div className="mb-3">
                        <label htmlFor="kmRodados" className="form-label">Km rodados</label>
                        <Controller
                            control={control}
                            name="kmRodados"
                            rules={{
                                required: "Campo obrigatório",
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
                                required: "Preço obrigatório",
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
                        <label htmlFor="image">Imagem do Veículo:</label>
                        <Controller
                            name="image"
                            control={control}
                            rules={{ required: 'Imagem obrigatória' }}
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

                    <input type="submit" value="Anunciar veículo" />
                </form>
            </div>
        </Layout>
    )
}