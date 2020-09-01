package io.github.gabrielsalesls.domain.service;

import java.time.OffsetDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.github.gabrielsalesls.domain.exception.EntidadeNaoEncontradaException;
import io.github.gabrielsalesls.domain.exception.NegocioException;
import io.github.gabrielsalesls.domain.model.Cliente;
import io.github.gabrielsalesls.domain.model.Comentario;
import io.github.gabrielsalesls.domain.model.OrdemServico;
import io.github.gabrielsalesls.domain.model.StatusOrdemServico;
import io.github.gabrielsalesls.domain.repository.ClienteRepository;
import io.github.gabrielsalesls.domain.repository.ComentarioRepository;
import io.github.gabrielsalesls.domain.repository.OrdemServicoRepository;

@Service
public class GestaoOrdemServicoService {
	
	@Autowired
	private OrdemServicoRepository ordemServicoRepository;
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private ComentarioRepository comentarioRepository;
	
	public OrdemServico criar(OrdemServico ordemServico) {
		Cliente cliente = clienteRepository.findById(ordemServico.getCliente().getId())
				.orElseThrow(() -> new NegocioException("Cliente não encontrado"));
		
		ordemServico.setCliente(cliente);
		ordemServico.setStatus(StatusOrdemServico.ABERTA);
		ordemServico.setDataAbertura(OffsetDateTime.now());
		
		return ordemServicoRepository.save(ordemServico);
	}
	
	public void finalizar(Long ordemServicoId) {
		OrdemServico ordemServico = buscar(ordemServicoId);
		
		ordemServico.finalizar();
		
		ordemServicoRepository.save(ordemServico);
				
	}
	
	public Comentario adicionarComentario(Long ordemServicoId, String descricao) {

		OrdemServico ordemServico = buscar(ordemServicoId);
		
		Comentario comentario = new Comentario();
		comentario.setDataEnvio(OffsetDateTime.now());
		comentario.setDescricao(descricao);
		comentario.setOrdemServico(ordemServico);
		
		return comentarioRepository.save(comentario);	
		
	}

	private OrdemServico buscar(Long ordemServicoId) {
		return ordemServicoRepository.findById(ordemServicoId)
				.orElseThrow(() -> new EntidadeNaoEncontradaException("Ordem de serviço não encontrada"));
	}

}
