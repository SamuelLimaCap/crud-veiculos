import { FC, useState } from 'react';

interface FiltroModeloProps {
  onFilter?: (modelo: string) => void;
}

const FiltroModelo: FC<FiltroModeloProps> = ({ onFilter }) => {
  const [modelo, setModelo] = useState('');

  const handleFilter = () => {
    if (onFilter && modelo) {
      onFilter(modelo);
    }
  };

  return (
    <div className='d-flex justify-content-center p-5'>
      <div className="row me-5">
        <label htmlFor="modelo">Modelo</label>
        <input
          type="text"
          name="modelo"
          id="modelo"
          value={modelo}
          onChange={(e) => setModelo(e.target.value)}
        />
      </div>
      <button onClick={handleFilter} className="btn btn-primary ms-3">Aplicar</button>
    </div>
  );
};

export default FiltroModelo;