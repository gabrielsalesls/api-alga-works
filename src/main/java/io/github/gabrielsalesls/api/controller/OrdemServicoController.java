package io.github.gabrielsalesls.api.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.github.gabrielsalesls.api.model.OrdemServicoInput;
import io.github.gabrielsalesls.api.model.OrdemServicoRepresetationModel;
import io.github.gabrielsalesls.domain.model.OrdemServico;
import io.github.gabrielsalesls.domain.repository.OrdemServicoRepository;
import io.github.gabrielsalesls.domain.service.GestaoOrdemServicoService;

@RestController
@RequestMapping("/ordens-servico")
public class OrdemServicoController {

	@Autowired
	private GestaoOrdemServicoService gestaoOrdServico;
	
	@Autowired
	private OrdemServicoRepository ordemServicoRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public OrdemServicoRepresetationModel criar(@Valid @RequestBody OrdemServicoInput ordemServicoInput) {
		OrdemServico ordemServico = toEntity(ordemServicoInput);
		
		return toModel(gestaoOrdServico.criar(ordemServico));
	}
	
	
	@GetMapping
	public List<OrdemServicoRepresetationModel> listar(){
		return toCollectionModel(ordemServicoRepository.findAll());
	}
	
	
	@GetMapping("/{ordemServicoId}")
	public ResponseEntity<OrdemServicoRepresetationModel> buscar(@PathVariable Long ordemServicoId){
		Optional<OrdemServico> ordemServico = ordemServicoRepository.findById(ordemServicoId);
		
		if(ordemServico.isPresent()) {
			OrdemServicoRepresetationModel ordemServicoModel = toModel(ordemServico.get());
			return ResponseEntity.ok(ordemServicoModel);
		}
		
		return ResponseEntity.notFound().build();
		
	}
	
	@PutMapping("/{ordemServicoId}/finalizacao")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void finalizar(@PathVariable Long ordemServicoId) {
		gestaoOrdServico.finalizar(ordemServicoId);
	}
	
	
	private OrdemServicoRepresetationModel toModel(OrdemServico ordemServico) {
		return modelMapper.map(ordemServico, OrdemServicoRepresetationModel.class);
	}
	
	private List<OrdemServicoRepresetationModel> toCollectionModel(List<OrdemServico> ordensServico) {
		return ordensServico.stream()
				.map(ordemServico -> toModel(ordemServico))
				.collect(Collectors.toList());
	}
	
	private OrdemServico toEntity(OrdemServicoInput ordemServicoInput) {
		return modelMapper.map(ordemServicoInput, OrdemServico.class);
	}
	
}





