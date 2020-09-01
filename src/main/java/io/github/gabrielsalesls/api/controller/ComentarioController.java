package io.github.gabrielsalesls.api.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.github.gabrielsalesls.api.model.ComentarioInput;
import io.github.gabrielsalesls.api.model.ComentarioRepresentationModel;
import io.github.gabrielsalesls.domain.exception.EntidadeNaoEncontradaException;
import io.github.gabrielsalesls.domain.model.Comentario;
import io.github.gabrielsalesls.domain.model.OrdemServico;
import io.github.gabrielsalesls.domain.repository.OrdemServicoRepository;
import io.github.gabrielsalesls.domain.service.GestaoOrdemServicoService;

@RestController
@RequestMapping("/ordens-servico/{ordemServicoId}/comentarios")
public class ComentarioController {

	@Autowired
	private GestaoOrdemServicoService gestaoOrdemServico;
	
	@Autowired
	private OrdemServicoRepository ordemServicoRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@GetMapping
	public List<ComentarioRepresentationModel> listar(@PathVariable Long ordemServicoId){
		OrdemServico ordemServico = ordemServicoRepository.findById(ordemServicoId)
				.orElseThrow(() -> new EntidadeNaoEncontradaException("Ordem de serviço não encontrada"));
		
		return toCollectionModel(ordemServico.getComentarios());
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ComentarioRepresentationModel adicionar(@PathVariable Long ordemServicoId,
			@Valid @RequestBody ComentarioInput comentarioInput) {
		
		Comentario comentario = gestaoOrdemServico.adicionarComentario(ordemServicoId, comentarioInput.getDescricao());
		
		return toModel(comentario);
		
	}
	
	private ComentarioRepresentationModel toModel(Comentario comentario) {
		return modelMapper.map(comentario, ComentarioRepresentationModel.class);
	}

	
	private List<ComentarioRepresentationModel> toCollectionModel(List<Comentario> comentarios) {
		return comentarios.stream()
				.map(comentario -> toModel(comentario))
				.collect(Collectors.toList());
	}
}






