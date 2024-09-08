package TGIDTeste.TesteTGID01.Service;

import TGIDTeste.TesteTGID01.Model.Empresa.empresa;
import TGIDTeste.TesteTGID01.Model.Transacoes.transacoes;
import TGIDTeste.TesteTGID01.Model.Sistema.sistema;
import TGIDTeste.TesteTGID01.Repository.EmpresaRepository;
import TGIDTeste.TesteTGID01.Repository.SistemaRepository;
import TGIDTeste.TesteTGID01.Repository.TransacoesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class TransacoesService {

    private final EmpresaRepository empresaRepository;
    private final TransacoesRepository transacoesRepository;
    private final SistemaRepository sistemaRepository;
    private final RestTemplate restTemplate;
    private final JavaMailSender emailSender;

    @Autowired
    public TransacoesService(EmpresaRepository empresaRepository, TransacoesRepository transacoesRepository, SistemaRepository sistemaRepository, RestTemplate restTemplate, JavaMailSender emailSender) {
        this.empresaRepository = empresaRepository;
        this.transacoesRepository = transacoesRepository;
        this.sistemaRepository = sistemaRepository;
        this.restTemplate = restTemplate;
        this.emailSender = emailSender;
    }

    @Transactional
    public void realizarTransacao(transacoes transacoes) {


        //Valida se a empresa existe
        if (transacoes.getEmpresa() == null || transacoes.getEmpresa().getCnpj() == null) {
            throw new IllegalArgumentException("Empresa ou CNPJ não fornecido.");
        }

        //Procura empresa no repositorio
        Optional<empresa> empresaOptional = empresaRepository.findByCnpj(transacoes.getEmpresa().getCnpj());
        if (!empresaOptional.isPresent()) {
            throw new IllegalArgumentException("Empresa com o CNPJ fornecido não encontrada!");
        }


        empresa empresa = empresaOptional.get();
        Double saldoAtual = empresa.getSaldo() != null ? empresa.getSaldo() : 0.0;
        Double tax = 0.03; // Taxa de transações

        //Inicia o registro da tabela de lucros para controle de ganhos do sistema
        if (sistemaRepository.countById() == 0) {
            sistema sistema = new sistema();
            sistema.setId("1");
            sistema.setLucro(0.0);
            sistemaRepository.save(sistema);
        }

        // Busca o valor do lucro no repositorio
        Double lucroAcumulado = sistemaRepository.findLucro().orElse(0.0);

        // Calcula o lucro da taxa de depositos e ajusta o saldo da empresa
        if (transacoes.getDeposito() != null) {
            Double deposito = transacoes.getDeposito();
            Double lucroSobreDeposito = tax * deposito;
            saldoAtual += deposito - lucroSobreDeposito;
            lucroAcumulado += lucroSobreDeposito; // Acumula o lucro
        }

        // Calcula o lucro da taxa de saques e atualiza o saldo da empresa
        if (transacoes.getSaque() != null) {
            Double saque = transacoes.getSaque();
            Double lucroSobreSaque = tax * saque;

            // Verifica se empresa possui saldo para a transação
            if (saldoAtual < saque + lucroSobreSaque) {
                throw new IllegalArgumentException("Saldo insuficiente para realizar o saque.");
            }

            //contador
            saldoAtual -= saque + lucroSobreSaque;
            lucroAcumulado += lucroSobreSaque;
        }

        // Atualiza os valores na empresa
        empresa.setSaldo(saldoAtual);
        empresaRepository.save(empresa);

        // Atualiza o lucro no banco de dados
        sistemaRepository.updateLucro(lucroAcumulado);

        // Salva
        transacoesRepository.save(transacoes);

        // Envio do callback para a empresa
        String callbackUrl = "https://webhook.site/5d764f82-8928-4c19-b2ba-0c00d518bf51";
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(callbackUrl, transacoes, String.class);
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("Falha ao enviar o callback");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Envio para o cliente
        enviarNotificacaoParaCliente(transacoes);
    }

    //envio do e-mail
    private void enviarNotificacaoParaCliente(transacoes transacoes) {
        String emailCliente = transacoes.getEmpresa().getEmail();
        String mensagem = "Sua transação foi realizada com sucesso. Detalhes: " + transacoes.toString();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailCliente);
        message.setSubject("Notificação de Transação");
        message.setText(mensagem);
        emailSender.send(message);
    }
}