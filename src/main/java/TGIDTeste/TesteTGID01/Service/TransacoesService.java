package TGIDTeste.TesteTGID01.Service;

import TGIDTeste.TesteTGID01.Model.Empresa.empresa;
import TGIDTeste.TesteTGID01.Model.Transacoes.transacoes;
import TGIDTeste.TesteTGID01.Model.Sistema.sistema;
import TGIDTeste.TesteTGID01.Repository.EmpresaRepository;
import TGIDTeste.TesteTGID01.Repository.SistemaRepository;
import TGIDTeste.TesteTGID01.Repository.TransacoesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TransacoesService {

    private final EmpresaRepository empresaRepository;
    private final TransacoesRepository transacoesRepository;
    private final SistemaRepository sistemaRepository;

    @Autowired
    public TransacoesService(EmpresaRepository empresaRepository, TransacoesRepository transacoesRepository, SistemaRepository sistemaRepository) {
        this.empresaRepository = empresaRepository;
        this.transacoesRepository = transacoesRepository;
        this.sistemaRepository = sistemaRepository;
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
        Double tax = 0.03;

        if (sistemaRepository.countById() == 0) {
            sistema sistema = new sistema();
            sistema.setId("1");
            sistema.setLucro(0.0);
            sistemaRepository.save(sistema);
        }

        Double lucroAcumulado = sistemaRepository.findLucro().orElse(0.0);

        if (transacoes.getDeposito() != null) {
            Double deposito = transacoes.getDeposito();
            Double lucroSobreDeposito = tax * deposito;
            saldoAtual += deposito - lucroSobreDeposito;
            lucroAcumulado += lucroSobreDeposito;
        }

        if (transacoes.getSaque() != null) {
            Double saque = transacoes.getSaque();
            Double lucroSobreSaque = tax * saque;

            if (saldoAtual < saque + lucroSobreSaque) {
                throw new IllegalArgumentException("Saldo insuficiente para realizar o saque.");
            }

            saldoAtual -= saque + lucroSobreSaque;
            lucroAcumulado += lucroSobreSaque;
        }

        empresa.setSaldo(saldoAtual);
        empresaRepository.save(empresa);

        sistemaRepository.updateLucro(lucroAcumulado);

        transacoesRepository.save(transacoes);
    }
}