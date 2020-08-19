package io.github.gabrielsalesls.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.github.gabrielsalesls.domain.exception.NegocioException;
import io.github.gabrielsalesls.domain.model.Cliente;
import io.github.gabrielsalesls.domain.repository.ClienteRepository;

@Service
public class CadastroClienteService {
	
	// ALTERAÇÕES NO BANCO DE DADOS PASSAM PELO SERVICE
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	public Cliente salvar(Cliente cliente) {
		Cliente clienteExistente = clienteRepository.findByEmail(cliente.getEmail());
		
		if (clienteExistente != null && !clienteExistente.equals(cliente)) {
			throw new NegocioException("Já existe um cliente cadastrado com esse e-mail.");
		}
		
		return clienteRepository.save(cliente);
		
	}
	
	
	public void excluir(Long clienteId) {
		clienteRepository.deleteById(clienteId);
	}

}
