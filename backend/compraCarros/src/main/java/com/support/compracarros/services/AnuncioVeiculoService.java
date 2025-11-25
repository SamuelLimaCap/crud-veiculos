package com.support.compracarros.services;

import com.support.compracarros.dto.handlers.PageSuccessResponse;
import com.support.compracarros.dto.req.CreateAnuncioVeiculoReq;
import com.support.compracarros.dto.req.UpdateAnuncioVeiculoReq;
import com.support.compracarros.dto.res.AnuncioVeiculoRes;
import com.support.compracarros.models.AnuncioVeiculoState;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AnuncioVeiculoService {

    public AnuncioVeiculoRes anunciarVeiculo(CreateAnuncioVeiculoReq anuncioVeiculoReq);

    public AnuncioVeiculoRes updateVeiculo(UpdateAnuncioVeiculoReq anuncioVeiculoReq);

    public boolean mudarEstadoAnuncio(AnuncioVeiculoState estado, Long anuncioId);

    public boolean deletarAnuncio(Long anuncioId);

    public PageSuccessResponse<List<AnuncioVeiculoRes>> getAll(int page, int size);

    public AnuncioVeiculoRes findById(Long anuncioId);


}
