import { FC } from 'react';

interface FiltroOrdenarProps {
  onSelect?: (value: string) => void;
}

const FiltroOrdenar: FC<FiltroOrdenarProps> = ({ onSelect }) => {
  const handleChange = (value: string) => {
    if (onSelect) onSelect(value);
  };

  return (
    <div>
      <p>
        <input type="radio" name="filtro-ordenar" value="menor-preco" onChange={(e) => handleChange(e.target.value)} />
        <label>Menor preço</label>
      </p>
      <p>
        <input type="radio" name="filtro-ordenar" value="menor-km" onChange={(e) => handleChange(e.target.value)} />
        <label>Menor km</label>
      </p>
      <p>
        <input type="radio" name="filtro-ordenar" value="menor-ano" onChange={(e) => handleChange(e.target.value)} />
        <label>Mais antigo</label>
      </p>
      <p>
        <input type="radio" name="filtro-ordenar" value="maior-preco" onChange={(e) => handleChange(e.target.value)} />
        <label>Maior preço</label>
      </p>
      <p>
        <input type="radio" name="filtro-ordenar" value="maior-km" onChange={(e) => handleChange(e.target.value)} />
        <label>Maior km</label>
      </p>
      <p>
        <input type="radio" name="filtro-ordenar" value="maior-ano" onChange={(e) => handleChange(e.target.value)} />
        <label>Mais novo</label>
      </p>
    </div>
  );
};

export default FiltroOrdenar;