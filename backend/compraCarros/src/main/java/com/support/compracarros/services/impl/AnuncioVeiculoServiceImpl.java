package com.support.compracarros.services.impl;

import com.support.compracarros.dto.handlers.PageSuccessResponse;
import com.support.compracarros.dto.handlers.Result;
import com.support.compracarros.dto.req.CreateAnuncioVeiculoReq;
import com.support.compracarros.dto.req.UpdateAnuncioVeiculoReq;
import com.support.compracarros.dto.res.AnuncioVeiculoRes;
import com.support.compracarros.entities.AnuncioVeiculo;
import com.support.compracarros.entities.Veiculo;
import com.support.compracarros.models.AnuncioVeiculoState;
import com.support.compracarros.models.UpdateAnuncioType;
import com.support.compracarros.repositories.AnuncioVeiculoRepository;
import com.support.compracarros.repositories.UserRepository;
import com.support.compracarros.repositories.VeiculoRepository;
import com.support.compracarros.services.AnuncioVeiculoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnuncioVeiculoServiceImpl implements AnuncioVeiculoService {

    private final AnuncioVeiculoRepository anuncioVeiculoRepository;
    private final VeiculoRepository veiculoRepository;
    private final UserRepository userRepository;


    @Override
    public AnuncioVeiculoRes anunciarVeiculo(CreateAnuncioVeiculoReq anuncioVeiculoReq) throws IOException {

        System.out.println(anuncioVeiculoReq);
        if (anuncioVeiculoRepository.existsByPlacaAndDeletadoFalse(anuncioVeiculoReq.getPlaca())) {
            throw new RuntimeException("Essa placa do carro já está cadastrada em 1 anuncio de compra");
        }

        var email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("user not found"));

        var veiculo = Veiculo.builder()
                .marca(anuncioVeiculoReq.getMarca())
                .modelo(anuncioVeiculoReq.getModelo())
                .ano(anuncioVeiculoReq.getAno())
                .combustivel(anuncioVeiculoReq.getCombustivel())
                .cor(anuncioVeiculoReq.getCor())
                .build();

        System.out.println(veiculo);

        var veiculoEntity = veiculoRepository.save(veiculo);
        var anuncioVeiculoBuilder = AnuncioVeiculo.builder()
                .user(user)
                .veiculo(veiculoEntity)
                .placa(anuncioVeiculoReq.getPlaca())
                .price(anuncioVeiculoReq.getPrice())
                .kmRodados(anuncioVeiculoReq.getKmRodados())
                .estado(AnuncioVeiculoState.ABERTO);

        if (anuncioVeiculoReq.getImage() != null) {

            anuncioVeiculoBuilder.imageName(anuncioVeiculoReq.getImage().getOriginalFilename())
                    .imageType(anuncioVeiculoReq.getImage().getContentType())
                    .imageData(anuncioVeiculoReq.getImage().getBytes());
        }

        var anuncioVeiculo = anuncioVeiculoBuilder.build();
        System.out.println(anuncioVeiculo);
        anuncioVeiculoRepository.save(anuncioVeiculo);

        return AnuncioVeiculoRes.of(anuncioVeiculo);
    }

    @Override
    public AnuncioVeiculoRes updateVeiculo(UpdateAnuncioVeiculoReq anuncioVeiculoReq) throws IOException {
        AnuncioVeiculo newAnuncioVeiculo;
        if (anuncioVeiculoReq.getType().equals(UpdateAnuncioType.PUT)) {
            newAnuncioVeiculo = updateVeiculoViaPut(anuncioVeiculoReq);
        } else {
            newAnuncioVeiculo = updateVeiculoViaPatch(anuncioVeiculoReq);
        }

        anuncioVeiculoRepository.save(newAnuncioVeiculo);

        return AnuncioVeiculoRes.of(newAnuncioVeiculo);
    }

    @Override
    public boolean mudarEstadoAnuncio(AnuncioVeiculoState estado, Long anuncioId, Optional<Long> usuarioIdEmNegociacao) {
        var anuncio = anuncioVeiculoRepository.findById(anuncioId).orElseThrow(() -> new IllegalArgumentException("Id invalido"));

        if (anuncio.getEstado().equals(AnuncioVeiculoState.ENCERRADO)) {
            throw new RuntimeException("Não pode alterar o estado de um anuncio encerrado");
        }

        var userEmNegociacao = userRepository.findById(usuarioIdEmNegociacao.get()).orElseThrow(() -> new IllegalArgumentException("Id invalido"));

        anuncio.setEstado(estado);
        anuncio.setUserEmNegociacao(userEmNegociacao);
        anuncioVeiculoRepository.save(anuncio);
        return true;
    }

    @Override
    public boolean deletarAnuncio(Long anuncioId) {
        var anuncio = anuncioVeiculoRepository.findById(anuncioId).orElseThrow(() -> new IllegalArgumentException("Id invalido"));
        var estadoAnuncio = anuncio.getEstado();

        if (estadoAnuncio.equals(AnuncioVeiculoState.ENCERRADO)) {
            throw new RuntimeException("Não pode deletar um anuncio encerrado ou em negociação");
        }

        anuncio.setDeletado(true);
        anuncioVeiculoRepository.save(anuncio);
        return true;
    }

    @Override
    public PageSuccessResponse<List<AnuncioVeiculoRes>> getAll(int page, int size) {
        var pageAnuncioList = anuncioVeiculoRepository.findByDeletadoFalse(PageRequest.of(page, size));

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

    @Override
    public List<AnuncioVeiculoRes> findByCreatedUserId(Long idUser) {
        System.out.println(idUser);
        return anuncioVeiculoRepository.findByUser_IdAndDeletadoFalse(idUser).stream().map(AnuncioVeiculoRes::of).collect(Collectors.toList());
    }

    @Override
    public PageSuccessResponse<List<AnuncioVeiculoRes>> filtrar(int page, int size, String ordenarPor, BigDecimal precoMin, BigDecimal precoMax, BigDecimal kmMin, BigDecimal kmMax, Integer anoMin, Integer anoMax, String marca, String modelo) {

        Specification<AnuncioVeiculo> spec = (root, query, cb) -> cb.equal(root.get("deletado"), false);

        spec = spec.and((root, query, cb) -> cb.equal(root.get("deletado"), false));

        if (precoMin != null) {
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("price"), precoMin));
        }
        if (precoMax != null) {
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("price"), precoMax));
        }

        if (kmMin != null) {
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("kmRodados"), kmMin));
        }
        if (kmMax != null) {
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("kmRodados"), kmMax));
        }

        if (anoMin != null) {
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("veiculo").get("ano"), anoMin));
        }
        if (anoMax != null) {
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("veiculo").get("ano"), anoMax));
        }

        if (marca != null && !marca.isBlank()) {
            spec = spec.and((root, query, cb) -> cb.like(
                    cb.lower(root.get("veiculo").get("marca")),
                    "%" + marca.toLowerCase() + "%"
            ));
        }

        if (modelo != null && !modelo.isBlank()) {
            spec = spec.and((root, query, cb) -> cb.like(
                    cb.lower(root.get("veiculo").get("modelo")),
                    "%" + modelo.toLowerCase() + "%"
            ));
        }

        Sort sort = Sort.by("createdAt").descending();

        if (ordenarPor != null) {
            sort = switch (ordenarPor) {
                case "menor-preco" -> Sort.by("price").ascending();
                case "maior-preco" -> Sort.by("price").descending();
                case "menor-km" -> Sort.by("kmRodados").ascending();
                case "maior-km" -> Sort.by("kmRodados").descending();
                case "menor-ano" -> Sort.by("veiculo.ano").ascending();
                case "maior-ano" -> Sort.by("veiculo.ano").descending();
                default -> Sort.by("createdAt").descending();
            };
        }

        Pageable pageable = PageRequest.of(page, size, sort);
        var pageAnuncioList = anuncioVeiculoRepository.findAll(spec, pageable);

        return Result.withPage("sucesso",
                pageAnuncioList.getContent().stream().map(AnuncioVeiculoRes::of).collect(Collectors.toList()),
                new PageSuccessResponse.PageDetails(page, size, pageAnuncioList.getTotalElements(), pageAnuncioList.getTotalPages())
        );
    }

    private AnuncioVeiculo updateVeiculoViaPut(UpdateAnuncioVeiculoReq anuncioVeiculoReq) {
        System.out.println("entrou aqui");
        var anuncio = anuncioVeiculoRepository.findById(anuncioVeiculoReq.getId()).orElseThrow(() -> new RuntimeException("Anuncio nao encontrado"));

        anuncio.setPlaca(anuncioVeiculoReq.getPlaca());
        anuncio.setPrice(anuncioVeiculoReq.getPrice());
        anuncio.setEstado(anuncioVeiculoReq.getEstado());

        var veiculo = anuncio.getVeiculo();
        veiculo.setMarca(anuncioVeiculoReq.getMarca());
        veiculo.setModelo(anuncioVeiculoReq.getModelo());
        veiculo.setAno(anuncioVeiculoReq.getAno());
        veiculo.setCor(anuncioVeiculoReq.getCor());

        anuncio.setVeiculo(veiculo);

        return anuncio;
    }

    private AnuncioVeiculo updateVeiculoViaPatch(UpdateAnuncioVeiculoReq anuncioVeiculoReq) throws IOException {
        var anuncio = anuncioVeiculoRepository.findById(anuncioVeiculoReq.getId()).orElseThrow(() -> new RuntimeException("Anuncio nao encontrado"));

        if (anuncioVeiculoReq.getPlaca() != null && !anuncioVeiculoReq.getPlaca().isBlank()) {
            if (anuncioVeiculoRepository.existsByPlacaAndDeletadoFalse(anuncioVeiculoReq.getPlaca())) {
                throw new IllegalArgumentException("Placa já cadastrada em outro anuncio");
            }
            anuncio.setPlaca(anuncioVeiculoReq.getPlaca());
        }
        if (anuncioVeiculoReq.getPrice() != null)
            anuncio.setPrice(anuncioVeiculoReq.getPrice());
        if (anuncioVeiculoReq.getEstado() != null)
            anuncio.setEstado(anuncioVeiculoReq.getEstado());
        if (anuncioVeiculoReq.getImage() != null) {
            var imageReq =  anuncioVeiculoReq.getImage();
            anuncio.setImageName(imageReq.getName());
            anuncio.setImageType(imageReq.getContentType());
            anuncio.setImageData(imageReq.getBytes());
        }

        var veiculo = anuncio.getVeiculo();
        if (anuncioVeiculoReq.getMarca() != null)
            veiculo.setMarca(anuncioVeiculoReq.getMarca());
        if (anuncioVeiculoReq.getModelo() != null)
            veiculo.setModelo(anuncioVeiculoReq.getModelo());
        if (anuncioVeiculoReq.getAno() != null)
            veiculo.setAno(anuncioVeiculoReq.getAno());
        if (anuncioVeiculoReq.getCor() != null && !anuncioVeiculoReq.getCor().isBlank())
            veiculo.setCor(anuncioVeiculoReq.getCor());

        return anuncio;
    }

}
