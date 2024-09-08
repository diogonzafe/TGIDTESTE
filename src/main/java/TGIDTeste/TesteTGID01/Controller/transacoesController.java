package TGIDTeste.TesteTGID01.Controller;

import TGIDTeste.TesteTGID01.Model.Cliente.cliente;
import TGIDTeste.TesteTGID01.Model.Empresa.empresa;
import TGIDTeste.TesteTGID01.Model.Transacoes.transacoes;
import TGIDTeste.TesteTGID01.Model.Transacoes.transacoesDTO;
import TGIDTeste.TesteTGID01.Repository.ClienteRepository;
import TGIDTeste.TesteTGID01.Repository.EmpresaRepository;
import TGIDTeste.TesteTGID01.Service.TransacoesService;
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
@RequestMapping("/transacoes")
public class transacoesController {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private TransacoesService transacoesService;

    @PostMapping
    public ResponseEntity<String> addSaldo(@RequestBody @Valid transacoesDTO data) {
        Optional<empresa> empresaOpt = empresaRepository.findByCnpj(data.cnpj());
        Optional<cliente> clienteOpt = clienteRepository.findByCpf(data.cpf());

        if (!empresaOpt.isPresent() || !clienteOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Dados não encontrados");
        }

        empresa empresa = empresaOpt.get();
        cliente cliente = clienteOpt.get();

        transacoes transacao = new transacoes(data, cliente, empresa);

        try {
            transacoesService.realizarTransacao(transacao);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

        return ResponseEntity.ok("Transação realizada com sucesso");
    }
}