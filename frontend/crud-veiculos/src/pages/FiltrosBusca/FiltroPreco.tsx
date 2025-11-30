import { FC, useState } from 'react';

interface FiltroPrecoProp {
  onFilter?: (min: number, max: number) => void;
}

const FiltroPreco: FC<FiltroPrecoProp> = ({ onFilter }) => {
  const [precoMin, setPrecoMin] = useState('');
  const [precoMax, setPrecoMax] = useState('');

  const handleFilter = () => {
    if (onFilter && precoMin && precoMax) {
      onFilter(Number(precoMin), Number(precoMax));
    }
  };

  return (
    <div className='d-flex justify-content-center p-5'>
      <div className="row me-5">
        <label htmlFor="preco-min">Preço min</label>
        <input
          type="number"
          name="preco-min"
          id="preco-min"
          value={precoMin}
          onChange={(e) => setPrecoMin(e.target.value)}
        />
      </div>
      <div className="row">
        <label htmlFor="preco-max">Preço max</label>
        <input
          type="number"
          name="preco-max"
          id="preco-max"
          value={precoMax}
          onChange={(e) => setPrecoMax(e.target.value)}
        />
      </div>
      <button onClick={handleFilter} className="btn btn-primary ms-3">Aplicar</button>
    </div>
  );
};

export default FiltroPreco;