import { FC, useState } from 'react';

interface FiltroKmProps {
  onFilter?: (min: number, max: number) => void;
}

const FiltroKm: FC<FiltroKmProps> = ({ onFilter }) => {
  const [kmMin, setKmMin] = useState('');
  const [kmMax, setKmMax] = useState('');

  const handleFilter = () => {
    if (onFilter && kmMin && kmMax) {
      onFilter(Number(kmMin), Number(kmMax));
    }
  };

  return (
    <div className='d-flex justify-content-center p-5'>
      <div className="row me-5">
        <label htmlFor="km-min">Km min</label>
        <input
          type="number"
          name="km-min"
          id="km-min"
          value={kmMin}
          onChange={(e) => setKmMin(e.target.value)}
        />
      </div>
      <div className="row">
        <label htmlFor="km-max">Km max</label>
        <input
          type="number"
          name="km-max"
          id="km-max"
          value={kmMax}
          onChange={(e) => setKmMax(e.target.value)}
        />
      </div>
      <button onClick={handleFilter} className="btn btn-primary ms-3">Aplicar</button>
    </div>
  );
};

export default FiltroKm;