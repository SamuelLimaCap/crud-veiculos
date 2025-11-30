import { FC, useState } from 'react';

interface FiltroMarcaProps {
  onFilter?: (marca: string) => void;
}

const FiltroMarca: FC<FiltroMarcaProps> = ({ onFilter }) => {
  const [marca, setMarca] = useState('');

  const handleFilter = () => {
    if (onFilter && marca) {
      onFilter(marca);
    }
  };

  return (
    <div className='d-flex justify-content-center p-5'>
      <div className="row me-5">
        <label htmlFor="marca">Marca</label>
        <input
          type="text"
          name="marca"
          id="marca"
          value={marca}
          onChange={(e) => setMarca(e.target.value)}
        />
      </div>
      <button onClick={handleFilter} className="btn btn-primary ms-3">Aplicar</button>
    </div>
  );
};

export default FiltroMarca;