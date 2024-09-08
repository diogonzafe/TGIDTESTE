package TGIDTeste.TesteTGID01.Service;

import TGIDTeste.TesteTGID01.Model.Empresa.empresa;
import TGIDTeste.TesteTGID01.Model.Transacoes.transacoes;
import TGIDTeste.TesteTGID01.Repository.EmpresaRepository;
import TGIDTeste.TesteTGID01.Repository.TransacoesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TransacoesService {

    private final EmpresaRepository empresaRepository;
    private final TransacoesRepository transacoesRepository;

    @Autowired
    public TransacoesService(EmpresaRepository empresaRepository, TransacoesRepository transacoesRepository) {
        this.empresaRepository = empresaRepository;
        this.transacoesRepository = transacoesRepository;
    }

    @Transactional
    public void realizarTransacao(transacoes transacoes) {

        if (transacoes.getEmpresa() == null || transacoes.getEmpresa().getCnpj() == null) {
            throw new IllegalArgumentException("Empresa ou CNPJ não fornecido.");
        }

        Optional<empresa> empresaOptional = empresaRepository.findByCnpj(transacoes.getEmpresa().getCnpj());
        if (!empresaOptional.isPresent()) {
            throw new IllegalArgumentException("Empresa com o CNPJ fornecido não encontrada!");
        }

        empresa empresa = empresaOptional.get();
        Double saldoAtual = empresa.getSaldo() != null ? empresa.getSaldo() : 0.0;

        if (transacoes.getDeposito() != null) {
            saldoAtual += transacoes.getDeposito();
        }

        if (transacoes.getSaque() != null) {
            saldoAtual -= transacoes.getSaque();
        }

        empresa.setSaldo(saldoAtual);
        empresaRepository.save(empresa);
        transacoesRepository.save(transacoes);
    }
}