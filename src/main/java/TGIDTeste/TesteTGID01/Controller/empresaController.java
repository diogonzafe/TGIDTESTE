package TGIDTeste.TesteTGID01.Controller;

import TGIDTeste.TesteTGID01.Model.Empresa.empresa;
import TGIDTeste.TesteTGID01.Model.Empresa.empresaDTO;
import TGIDTeste.TesteTGID01.Repository.EmpresaRepository;
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
@RequestMapping("/empresa")
public class empresaController {
    @Autowired
    private EmpresaRepository empresaRepository;

    @PostMapping
    public ResponseEntity<?> registerEmpresa(@RequestBody @Valid empresaDTO data) {
        Optional<empresa> existingEmpresa = empresaRepository.findByCnpj(data.cnpj());

        if (existingEmpresa.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("CNPJ já cadastrado.");
        }

        empresa newEmpresa = new empresa(data);
        empresaRepository.save(newEmpresa);

        return ResponseEntity.ok().build();
    }
}