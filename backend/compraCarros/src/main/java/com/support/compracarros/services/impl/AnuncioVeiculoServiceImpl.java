package com.support.compracarros.services.impl;

import com.support.compracarros.dto.handlers.PageSuccessResponse;
import com.support.compracarros.dto.handlers.Result;
import com.support.compracarros.dto.req.CreateAnuncioVeiculoReq;
import com.support.compracarros.dto.req.UpdateAnuncioVeiculoReq;
import com.support.compracarros.dto.res.AnuncioVeiculoRes;
import com.support.compracarros.dto.res.VeiculoResponse;
import com.support.compracarros.entities.AnuncioVeiculo;
import com.support.compracarros.entities.Veiculo;
import com.support.compracarros.models.AnuncioVeiculoState;
import com.support.compracarros.models.UpdateAnuncioType;
import com.support.compracarros.repositories.AnuncioVeiculoRepository;
import com.support.compracarros.repositories.UserRepository;
import com.support.compracarros.repositories.VeiculoRepository;
import com.support.compracarros.services.AnuncioVeiculoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnuncioVeiculoServiceImpl implements AnuncioVeiculoService {

    private final AnuncioVeiculoRepository anuncioVeiculoRepository;
    private final VeiculoRepository veiculoRepository;
    private final UserRepository userRepository;


    @Override
    public AnuncioVeiculoRes anunciarVeiculo(CreateAnuncioVeiculoReq anuncioVeiculoReq) {

        System.out.println(anuncioVeiculoReq);
        if (anuncioVeiculoRepository.existsByPlaca(anuncioVeiculoReq.placa())) {
            throw new RuntimeException("Essa placa do carro já está cadastrada em 1 anuncio de compra");
        }

        var email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("user not found"));

        var veiculo = Veiculo.builder()
                .marca(anuncioVeiculoReq.marca())
                .modelo(anuncioVeiculoReq.modelo())
                .ano(anuncioVeiculoReq.ano())
                .cor(anuncioVeiculoReq.cor())
                .build();

        System.out.println(veiculo);

        var veiculoEntity = veiculoRepository.save(veiculo);
        var anuncioVeiculo = AnuncioVeiculo.builder()
                .user(user)
                .veiculo(veiculoEntity)
                .placa(anuncioVeiculoReq.placa())
                .price(anuncioVeiculoReq.price())
                .estado(AnuncioVeiculoState.ABERTO)
                .build();

        anuncioVeiculoRepository.save(anuncioVeiculo);


        return AnuncioVeiculoRes.of(anuncioVeiculo);
    }

    @Override
    public AnuncioVeiculoRes updateVeiculo(UpdateAnuncioVeiculoReq anuncioVeiculoReq) {
        AnuncioVeiculo newAnuncioVeiculo;
        if (anuncioVeiculoReq.type().equals(UpdateAnuncioType.PUT)) {
            newAnuncioVeiculo = updateVeiculoViaPut(anuncioVeiculoReq);
        } else {
            newAnuncioVeiculo = updateVeiculoViaPatch(anuncioVeiculoReq);
        }

        anuncioVeiculoRepository.save(newAnuncioVeiculo);

        return AnuncioVeiculoRes.of(newAnuncioVeiculo);
    }

    @Override
    public boolean mudarEstadoAnuncio(AnuncioVeiculoState estado, Long anuncioId) {
        var anuncio = anuncioVeiculoRepository.findById(anuncioId).orElseThrow(() -> new IllegalArgumentException("Id invalido"));

        if (anuncio.getEstado().equals(AnuncioVeiculoState.ENCERRADO)) {
            throw new RuntimeException("Não pode alterar o estado de um anuncio encerrado");
        }

        anuncio.setEstado(estado);
        anuncioVeiculoRepository.save(anuncio);
        return true;
    }

    @Override
    public boolean deletarAnuncio(Long anuncioId) {
        var anuncio = anuncioVeiculoRepository.findById(anuncioId).orElseThrow(() -> new IllegalArgumentException("Id invalido"));
        var estadoAnuncio = anuncio.getEstado();

        if (estadoAnuncio.equals(AnuncioVeiculoState.ENCERRADO)) {
            throw new RuntimeException("Não pode deletar um anuncio encerrado ou em negociação");
        } else if (estadoAnuncio.equals(AnuncioVeiculoState.EM_NEGOCIACAO)) {
            throw new RuntimeException("Não pode deletar um anuncio em negociação. Desista da negociação primeiro e depois delete o anuncio");
        }

        anuncio.setDetelado(true);
        anuncioVeiculoRepository.save(anuncio);
        return true;
    }

    @Override
    public PageSuccessResponse<List<AnuncioVeiculoRes>> getAll(int page, int size) {
        var pageAnuncioList = anuncioVeiculoRepository.findAllWhereDeletadoIsFalse(PageRequest.of(page, size));

        return Result.withPage("sucesso",
                pageAnuncioList.getContent().stream().map(AnuncioVeiculoRes::of).collect(Collectors.toList()),
                new PageSuccessResponse.PageDetails(page, size, pageAnuncioList.getTotalElements(), pageAnuncioList.getTotalPages())
        );


    }

    @Override
    public AnuncioVeiculoRes findById(Long anuncioId) {
        var anuncio = anuncioVeiculoRepository.findById(anuncioId).orElseThrow(() -> new IllegalArgumentException("Id invalido"));

        return  AnuncioVeiculoRes.of(anuncio);
    }

    private AnuncioVeiculo updateVeiculoViaPut(UpdateAnuncioVeiculoReq anuncioVeiculoReq) {
        System.out.println("entrou aqui");
        var anuncio = anuncioVeiculoRepository.findById(anuncioVeiculoReq.id()).orElseThrow(() -> new RuntimeException("Anuncio nao encontrado"));

        anuncio.setPlaca(anuncioVeiculoReq.placa());
        anuncio.setPrice(anuncioVeiculoReq.price());
        anuncio.setEstado(anuncioVeiculoReq.estado());

        var veiculo = anuncio.getVeiculo();
        veiculo.setMarca(anuncioVeiculoReq.marca());
        veiculo.setModelo(anuncioVeiculoReq.modelo());
        veiculo.setAno(anuncioVeiculoReq.ano());
        veiculo.setCor(anuncioVeiculoReq.cor());

        anuncio.setVeiculo(veiculo);

        return anuncio;
    }

    private AnuncioVeiculo updateVeiculoViaPatch(UpdateAnuncioVeiculoReq anuncioVeiculoReq) {
        var anuncio = anuncioVeiculoRepository.findById(anuncioVeiculoReq.id()).orElseThrow(() -> new RuntimeException("Anuncio nao encontrado"));

        if (anuncioVeiculoReq.placa() != null)
            anuncio.setPlaca(anuncioVeiculoReq.placa());
        if (anuncioVeiculoReq.price() != null)
            anuncio.setPrice(anuncioVeiculoReq.price());
        if (anuncioVeiculoReq.estado() != null)
            anuncio.setEstado(anuncioVeiculoReq.estado());

        var veiculo = anuncio.getVeiculo();
        if (anuncioVeiculoReq.marca() != null)
            veiculo.setMarca(anuncioVeiculoReq.marca());
        if (anuncioVeiculoReq.modelo() != null)
            veiculo.setModelo(anuncioVeiculoReq.modelo());
        if (anuncioVeiculoReq.ano() != null)
            veiculo.setAno(anuncioVeiculoReq.ano());
        if (anuncioVeiculoReq.cor() != null)
            veiculo.setCor(anuncioVeiculoReq.cor());

        return anuncio;
    }

}
