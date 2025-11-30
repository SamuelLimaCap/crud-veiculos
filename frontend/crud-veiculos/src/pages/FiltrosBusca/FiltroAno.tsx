import { FC, useState } from 'react';

interface FiltroAnoProps {
  onFilter?: (min: number, max: number) => void;
}

const FiltroAno: FC<FiltroAnoProps> = ({ onFilter }) => {
  const [anoMin, setAnoMin] = useState('');
  const [anoMax, setAnoMax] = useState('');

  const handleFilter = () => {
    if (onFilter && anoMin && anoMax) {
      onFilter(Number(anoMin), Number(anoMax));
    }
  };

  return (
    <div className='d-flex justify-content-center p-5'>
      <div className="row me-5">
        <label htmlFor="ano-min">Ano min</label>
        <input
          type="number"
          name="ano-min"
          id="ano-min"
          value={anoMin}
          onChange={(e) => setAnoMin(e.target.value)}
        />
      </div>
      <div className="row">
        <label htmlFor="ano-max">Ano max</label>
        <input
          type="number"
          name="ano-max"
          id="ano-max"
          value={anoMax}
          onChange={(e) => setAnoMax(e.target.value)}
        />
      </div>
      <button onClick={handleFilter} className="btn btn-primary ms-3">Aplicar</button>
    </div>
  );
};

export default FiltroAno;