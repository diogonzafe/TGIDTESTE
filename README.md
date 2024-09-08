# TGIDTESTE
Teste Técnico (TGID) - Vaga: Estágio Back-end

Olá, tudo bem? Vou comentar um pouco sobre e projeto e como usá-lo.

Vamos comecar com o cliente.

O endpoint do cliente é esse: http://localhost:8080/cliente

	{
		"cpf" : "333.666.999-80",
		"name": "Elizete Ferreira",
		"email" : "email@gmail.com"
	}

O CPF Possui um regex para validar se foi enviado no formato padrao
XXX.XXX.XXX-XX.

Feita a requisição o cliente é salvo no Banco de dados.

____________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________

Agora vamos para empresa.

O endpoint da empresa é esse: http://localhost:8080/empresa

	{
		"cnpj" : "00.000.000/0000-00",
		"name": "Scania",
		"email" : "digadiogooh@gmail.com"
	}

Onde também existe um regex para validar se está no formato correto
XX.XXX.XXX/XXXX-XX.

____________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________

Agora vamos para as transações.

segue abaixo o controller das transações.

O endpoint é esse: http://localhost:8080/transacoes

	{
	    "cpf": "333.666.999-80",
	    "cnpj" : "00.000.000/0000-00",
	    "deposito": 100.00,
	    "saque": 0.00
	}

Valida se o cpf e o cnpj ja foram cadastrados, e também adiciona o valor do saldo na tabela da empresa inserida na requisição. Por regra de negócio o sistema fica com 3% das transações seja depósito ou saque(Valor também descontado dentro do saldo da empresa), que são armazenados e contabilizados numa tabela a parte na coluna "lucros". As transações feitas ficam salvas, cada uma com seu ID para formar um tipo de histórico.

____________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________


WEBHOOK e MAILSENDER:

Ao fazer essa requisição é enviado um callback no WEBHOOK e um e-mail para o cliente, configuracões feitas na classe AppConfig.

MailSender:

Para usar basta modificar:

Dentro do AppConfig:
	
 	mailSender.setUsername("email@gmail.com");
	mailSender.setPassword("senha");

application.properties:
Inserir também seu e-mail e senha:
	
 	spring.mail.username=seuemail@gmail.com
	spring.mail.password=suasenha

Email de confirmação:
Sua transação foi realizada com sucesso. Detalhes: TGIDTeste.TesteTGID01.Model.Transacoes.transacoes@15ff9e10

![Screenshot 2024-09-08 at 16 29 20](https://github.com/user-attachments/assets/9e0d0cb7-1667-4416-8198-83394d06ce79)


WEBHOOK

Configuração Callback WEBHOOK:

*Inserir URL disposta no site

	 String callbackUrl = "https://webhook.site/5d764f82-8928-4c19-b2ba-0c00d518bf51";
	        try {
	            ResponseEntity<String> response = restTemplate.postForEntity(callbackUrl, transacoes, String.class);
	            if (!response.getStatusCode().is2xxSuccessful()) {
	                throw new RuntimeException("Falha ao enviar o callback");
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }

LOGSITE:
 
 ![Screenshot 2024-09-08 at 16 52 17](https://github.com/user-attachments/assets/20d2a8f9-2356-4e5b-afbf-232e8d3602e3)


 Resultado após requisições feitas corretamente:

	 {
	  "id": "0e51dff0-b996-43c0-805c-dc0d69adfe78",
	  "deposito": 100,
	  "saque": 10,
	  "cliente": {
	    "cpf": "000.000.000-00",
	    "email": "digadiogooh@gmail.com",
	    "name": "Diogo Gonzaga"
	  },
	  "empresa": {
	    "cnpj": "00.000.000/0000-00",
	    "name": "Voss Automotive",
	    "email": "digadiogooh@gmail.com",
	    "saldo": 86.7 //desconto das taxas
	  }
	}


INSOMNIA:

<img width="400" alt="Screenshot 2024-09-08 at 16 37 56" src="https://github.com/user-attachments/assets/419caa24-f927-4f4a-bef2-39287fcfbc31">

____________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________

Banco de Dados:

Utilizei o Postgres, segue tabelas abaixo para melhor compreensão dos models e dos controllers.
Para criar as tabelas utilizei a dependencia flyway migrations no Spring, onde posso controler as versões do banco através das migrations.

Dependencias:

		<dependency>
			<groupId>org.flywaydb</groupId>
			<artifactId>flyway-core</artifactId>
		</dependency>

  		<dependency>
			<groupId>org.postgresql</groupId>
			<artifactId>postgresql</artifactId>
			<scope>runtime</scope>
		</dependency>

Cliente:

	CREATE TABLE cliente (
	    cpf TEXT PRIMARY KEY UNIQUE NOT NULL,
	    email TEXT UNIQUE NOT NULL,
	    name TEXT NOT NULL
	);

 Empresa:

	CREATE TABLE empresa (
	    cnpj TEXT PRIMARY KEY UNIQUE NOT NULL,
	    name TEXT UNIQUE NOT NULL,
	    email TEXT UNIQUE NOT NULL,
	    saldo DECIMAL
	);
 
 Transacoes:

 	CREATE TABLE transacoes (
	        id TEXT PRIMARY KEY UNIQUE NOT NULL,
	        deposito DECIMAL NOT NULL,
	        saque DECIMAL NOT NULL,
	        cpf TEXT REFERENCES cliente (cpf),
	        cnpj TEXT REFERENCES empresa (cnpj)
	);

 Sistema:

 	CREATE TABLE sistema (
	        id TEXT PRIMARY KEY UNIQUE NOT NULL,
	        lucro DECIMAL NOT NULL
	);

 application.properties:

 	spring.application.name=TesteTGID01
	spring.datasource.url=jdbc:postgresql://localhost:5434/seudb
	spring.datasource.username=seuuser
	spring.datasource.password=suasenha
	spring.jpa.show-sql=true
	spring.jpa.generate-ddl=true
	spring.jpa.hibernate.ddl-auto=create
	spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation=true

 

Considerações Finais

Em sumo o sistema tem dois usuários, cliente e empresa ,e o sistema cobra uma taxa de 3% para cada transação, os clientes fazem depósitos pelas empresas de acordo com o saldo disponivel. Ao realizar uma transação é enviado um callback para empresa, utilizei o site webhook para isso, conforme solicitado, e usei o MailSender do Spring para enviar um e-mail de notifição para o cliente. As taxas coletadas ficam armazenados e somadas na tabela do sistema, como se fosse um saldo de lucros do sistema.

Esse projeto foi desenvolvido 100% em Java, utilizando o framework Spring Boot. Durante o desenvolvimento, busquei atender ao máximo os requisitos propostos, além de aprender bastante durante o processo.

Obrigado pela oportunidade!










