package com.support.compracarros.services.impl;

import com.support.compracarros.dto.handlers.Result;
import com.support.compracarros.dto.handlers.SuccessResponse;
import com.support.compracarros.dto.req.PedidoAnuncioReq;
import com.support.compracarros.dto.res.PedidoAnuncioRes;
import com.support.compracarros.entities.PedidoCompra;
import com.support.compracarros.models.AnuncioVeiculoState;
import com.support.compracarros.repositories.AnuncioVeiculoRepository;
import com.support.compracarros.repositories.PedidoCompraRepository;
import com.support.compracarros.repositories.UserRepository;
import com.support.compracarros.services.PedidoCompraService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoCompraServiceImpl implements PedidoCompraService {

    private final PedidoCompraRepository pedidoCompraRepository;
    private final AnuncioVeiculoRepository anuncioVeiculoRepository;
    private final UserRepository userRepository;

    @Override
    public PedidoAnuncioRes pedirCompra(PedidoAnuncioReq pedidoAnuncio) {

        var email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var anuncio = anuncioVeiculoRepository
                .findById(pedidoAnuncio.idAnuncio())
                .orElseThrow(() -> new RuntimeException("Esse anuncio de compra não existe"));

        if (anuncio.isDeletado()) throw new RuntimeException("Anuncio deletado");
        if (anuncio.getEstado().equals(AnuncioVeiculoState.ENCERRADO)) throw new RuntimeException("Anuncio encerrado");

        var shouldUseUserFullname = pedidoAnuncio.nome() == null;
        var shouldUseUserEmail = pedidoAnuncio.email() == null;

        var user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        pedidoCompraRepository.findByUser_IdAndAnuncioVeiculo_Id(user.getId(), pedidoAnuncio.idAnuncio())
                .ifPresent((data) -> {
                    throw new RuntimeException("Usuário ja iniciou um pedido neste anuncio. Cancele o outro pedido pra realizar um novo");
                });

        var pedidoCompra = PedidoCompra.builder()
                .user(userRepository.getReferenceByEmail(email))
                .fullname((shouldUseUserFullname) ? user.getUsername() : pedidoAnuncio.nome())
                .userEmail(shouldUseUserEmail ? user.getEmail() : pedidoAnuncio.email())
                .anuncioVeiculo(anuncio)
                .telephone(pedidoAnuncio.telefone())
                .message(pedidoAnuncio.mensagem())
                .build();

        pedidoCompraRepository.save(pedidoCompra);

        return PedidoAnuncioRes.of(pedidoCompra);
    }

    @Override
    public SuccessResponse<List<PedidoAnuncioRes>> getAll() {

        var email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        var pedidoCompraPage = pedidoCompraRepository.findAllByDisabledFalseAndUser_Id(user.getId());

        return Result.with(
                "Retornado com sucesso!",
                pedidoCompraPage.stream().map(PedidoAnuncioRes::of).toList()
        );

    }

    @Override
    public List<PedidoAnuncioRes> getAllByAnuncio(Long id) {
        return pedidoCompraRepository.findAllByDisabledFalseAndAnuncioVeiculo_Id(id).stream().map(PedidoAnuncioRes::of).toList();
    }

    @Override
    public void desistirDaCompra(Long id) {

        var email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));


        var pedidoCompra = pedidoCompraRepository.findById(id).orElseThrow(() -> new RuntimeException("Pedido de compra não encontrado com esse id"));

        if (pedidoCompra.isDisabled()) throw new RuntimeException("Pedido de compra já foi invalidado");

        if (!user.getId().equals(pedidoCompra.getUser().getId())) throw new RuntimeException("Você não pode deletar um pedido de outra pessoa");

        pedidoCompra.setDisabled(true);
        pedidoCompraRepository.save(pedidoCompra);
    }
}
