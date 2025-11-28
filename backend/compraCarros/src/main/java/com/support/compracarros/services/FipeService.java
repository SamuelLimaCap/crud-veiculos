package com.support.compracarros.services;

import com.support.compracarros.entities.Modelos;
import com.support.compracarros.models.FipeResponseAno;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FipeService {

    public List<Modelos> getModelosByMarcaId(String marcaId);

    public List<FipeResponseAno> getAnosByModeloId(String modeloId, String marcaId);
}
