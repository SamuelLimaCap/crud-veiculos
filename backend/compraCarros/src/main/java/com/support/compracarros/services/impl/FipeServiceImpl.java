package com.support.compracarros.services.impl;

import com.support.compracarros.entities.Modelos;
import com.support.compracarros.models.FipeResponseAno;
import com.support.compracarros.repositories.ModelosRepository;
import com.support.compracarros.services.FipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FipeServiceImpl implements FipeService {

    private final RestTemplate restTemplate;
    private static final String FIPE_BASE_URL = "https://parallelum.com.br/fipe/api/v1/carros";

    private final ModelosRepository modelosRepository;

    @Override
    public List<Modelos> getModelosByMarcaId(String marcaId) {
        return modelosRepository.findAllByMarca_Marca(marcaId);
    }

    @Override
    public List<FipeResponseAno> getAnosByModeloId(String modeloId, String marcaId) {
        try {
            String url = String.format("%s/marcas/%s/modelos/%s/anos", FIPE_BASE_URL, marcaId, modeloId);

            FipeResponseAno[] response = restTemplate.getForObject(url, FipeResponseAno[].class);

            assert response != null;
            return java.util.Arrays.asList(response);
        } catch (RestClientException e) {
            throw new RuntimeException("Erro ao pegar o ano da API da fipe", e);
        }
    }
}
