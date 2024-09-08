package TGIDTeste.TesteTGID01.Controller;

import TGIDTeste.TesteTGID01.Model.Cliente.cliente;
import TGIDTeste.TesteTGID01.Model.Cliente.clienteDTO;
import TGIDTeste.TesteTGID01.Repository.ClienteRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/cliente")
public class clienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    @PostMapping
    public ResponseEntity<?> registerCliente(@RequestBody @Valid clienteDTO data) {
        Optional<cliente> existingCliente = clienteRepository.findByCpf(data.cpf());

        if (existingCliente.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("CPF já cadastrado.");
        }

        cliente newCliente = new cliente(data);
        clienteRepository.save(newCliente);

        return ResponseEntity.ok().build();
    }
}