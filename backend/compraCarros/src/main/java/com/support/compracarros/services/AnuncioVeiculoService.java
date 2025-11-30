package com.support.compracarros.services;

import com.support.compracarros.dto.handlers.PageSuccessResponse;
import com.support.compracarros.dto.req.CreateAnuncioVeiculoReq;
import com.support.compracarros.dto.req.UpdateAnuncioVeiculoReq;
import com.support.compracarros.dto.res.AnuncioVeiculoRes;
import com.support.compracarros.models.AnuncioVeiculoState;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public interface AnuncioVeiculoService {

    public AnuncioVeiculoRes anunciarVeiculo(CreateAnuncioVeiculoReq anuncioVeiculoReq) throws IOException;

    public AnuncioVeiculoRes updateVeiculo(UpdateAnuncioVeiculoReq anuncioVeiculoReq) throws IOException;

    public boolean mudarEstadoAnuncio(AnuncioVeiculoState estado, Long anuncioId, Optional<Long> usuarioIdEmNegociacao);

    public boolean deletarAnuncio(Long anuncioId);

    public PageSuccessResponse<List<AnuncioVeiculoRes>> getAll(int page, int size);

    public AnuncioVeiculoRes findById(Long anuncioId);


    public List<AnuncioVeiculoRes> findByCreatedUserId(Long idUser);
}
