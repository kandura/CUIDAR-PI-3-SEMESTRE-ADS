# CUIDAR — Versão 2.5 (Repositórios JDBC: Residente, Responsavel, ResidenteResponsavel)

Oitava versão. Adiciona três implementações JDBC, completando o trio de entidades que modelam o **lado humano da residência** (idoso + responsáveis legais) e a sua **associação N–N** com grau de parentesco.

## Mudanças desde a v2.4

| Nova classe | Implementa | Particularidade |
|---|---|---|
| `repository.impl.ResidenteRepositoryImpl` | `ResidenteRepository` | JOIN em `pessoa` + `quarto`; `buscarPorCpfPessoa` e `buscarPorNomePessoa` (ILIKE); `UPDATE` não troca a pessoa (só quarto, status e `obs_geral`) |
| `repository.impl.ResponsavelRepositoryImpl` | `ResponsavelRepository` | JOIN em `pessoa`; CRUD completo de endereço (`rua`, `numero`, `bairro`, `cidade`, `estado`, `cep`) |
| `repository.impl.ResidenteResponsavelRepositoryImpl` | `ResidenteResponsavelRepository` | Tabela associativa; SELECTs grandes com aliases (`resp_nome`, `res_nome`, `quarto_status`…) para `listarPorResidente` e `listarPorResponsavel`; vínculo se desfaz por `DELETE` (não por update lógico) |

Os SELECTs do `ResidenteResponsavelRepositoryImpl` precisam de **aliases diferentes para colunas homônimas** porque tanto `pessoa` do residente quanto `pessoa` do responsável trazem `nome_completo`, `cpf`, etc., e tanto `residente.status` quanto `quarto.status` viriam como `status`. Sem aliases, o `ResultSet` resolveria pela última coluna lida, corrompendo o objeto.

O `CuidarApp` agora lista os residentes (com quarto e status), os responsáveis (com cidade/UF e telefone) e os responsáveis vinculados ao primeiro residente da lista (com parentesco).

## Como rodar

```powershell
$lib = "C:\caminho\para\lib\postgresql-42.7.3.jar"
$files = (Get-ChildItem -Recurse src\main\java -Filter "*.java").FullName
javac -d out -cp $lib $files
Copy-Item src\main\resources\application.properties out\ -ErrorAction SilentlyContinue
java -cp "out;$lib" br.com.cuidar.CuidarApp
```

---

# CUIDAR — Versão 2.4 (Repositórios JDBC: Funcionario, Medico)

Sétima versão. Adiciona mais duas implementações JDBC, cobrindo entidades que **dependem de `Pessoa`** (e de `Cargo`, no caso de funcionário) e por isso precisam de **JOINs** para montar o objeto de domínio completo.

## Mudanças desde a v2.3

| Nova classe | Implementa | Particularidade |
|---|---|---|
| `repository.impl.FuncionarioRepositoryImpl` | `FuncionarioRepository` | JOINs em `pessoa` + `cargo`; `buscarPorLogin` (usado no login) e `buscarPorCpfPessoa` |
| `repository.impl.MedicoRepositoryImpl` | `MedicoRepository` | JOIN em `pessoa`; `buscarPorCrm` |

Os SELECTs retornam todas as colunas necessárias com aliases (`cargo_descricao`) para evitar conflito com nomes repetidos entre tabelas. O método auxiliar `montarFuncionario` / `montarMedico` constrói o objeto raiz com as referências aninhadas (`Pessoa`, `Cargo`).

O `CuidarApp` agora lista os funcionários (com login/cargo/turno) e os médicos cadastrados (com CRM e especialidade).

---

# CUIDAR — Versão 2.3 (Repositórios JDBC: Pessoa, Cargo, Quarto)

Sexta versão. Começa a **camada de persistência JDBC**: primeiras 3 implementações concretas dos repositórios, todas em `br.com.cuidar.repository.impl`. Cobrem as três entidades mais simples (sem FKs entre si): `Pessoa`, `Cargo` e `Quarto`.

Padrão adotado em todas as impls:
- `ConnectionFactory.getConnection()` no início, fechamento no `finally`
- `PreparedStatement` com parâmetros (sem concatenação SQL)
- `Statement.RETURN_GENERATED_KEYS` no `INSERT` para setar o ID gerado
- método auxiliar privado `montarXxx(ResultSet)` para mapear linha → objeto
- exceções `SQLException` são convertidas em `RuntimeException` com mensagem descritiva

## Mudanças desde a v2.2

| Nova classe | Implementa | Operações |
|---|---|---|
| `repository.impl.PessoaRepositoryImpl` | `PessoaRepository` | salvar, atualizar, buscarPorId, buscarPorCpf, listarTodos |
| `repository.impl.CargoRepositoryImpl` | `CargoRepository` | salvar, atualizar, buscarPorId, listarTodos |
| `repository.impl.QuartoRepositoryImpl` | `QuartoRepository` | salvar, atualizar, buscarPorId, listarTodos, listarPorStatus |

O `CuidarApp` agora instancia as 3 implementações e **lista** cargos, quartos e pessoas existentes no banco (operações somente leitura para não alterar dados do grupo).

---

# CUIDAR — Versão 2.2 (Interfaces de Repository)

Quinta versão. Define o **contrato de persistência** do sistema: 12 interfaces de Repository, uma para cada entidade do domínio. Cada interface declara apenas as assinaturas dos métodos de acesso a dados (salvar, atualizar, excluir, buscar, listar). A implementação JDBC virá a partir da v06.

## Mudanças desde a v2.1

12 novas interfaces em `br.com.cuidar.repository`:

| Interface | Métodos principais |
|---|---|
| `PessoaRepository` | salvar, atualizar, buscarPorId, **buscarPorCpf**, listarTodos |
| `CargoRepository` | salvar, atualizar, buscarPorId, listarTodos |
| `FuncionarioRepository` | salvar, atualizar, buscarPorId, **buscarPorCpfPessoa**, **buscarPorLogin**, listarTodos |
| `MedicoRepository` | salvar, atualizar, buscarPorId, **buscarPorCrm**, listarTodos |
| `QuartoRepository` | salvar, atualizar, buscarPorId, listarTodos, **listarPorStatus** |
| `ResidenteRepository` | salvar, atualizar, buscarPorId, **buscarPorCpfPessoa**, **buscarPorNomePessoa**, listarTodos |
| `ResponsavelRepository` | salvar, atualizar, buscarPorId, listarTodos |
| `ResidenteResponsavelRepository` | salvar, excluir, **listarPorResidente**, **listarPorResponsavel** |
| `ProntuarioRepository` | salvar, atualizar, buscarPorId, **buscarPorResidente** |
| `MedicamentoRepository` | salvar, atualizar, buscarPorId, listarTodos |
| `RegistroClinicoRepository` | salvar, excluir, buscarPorId, **listarPorResidente**, **listarPorPeriodo** |
| `AtividadeRepository` | salvar, atualizar, excluir, buscarPorId, listarTodos, **listarPorDiaSemana** |

O `CuidarApp` lista todas as interfaces (via reflection), conta os métodos de cada uma e testa a conexão com o banco.

---

# CUIDAR — Versão 2.1 (Configuração de banco)

Quarta versão. Adiciona a **camada de configuração de banco de dados**: dependência do driver JDBC do PostgreSQL no `pom.xml`, classe `ConnectionFactory` para centralizar a obtenção de conexões, arquivo `application.properties.example` para configuração local e o **schema SQL completo** (12 tabelas) em `resources/database/create_tables.sql`.

Ainda sem repositórios e sem UI. O `main` agora apenas tenta abrir uma conexão e exibe os metadados do banco.

## Mudanças desde a v1.3

| Novo arquivo | Função |
|---|---|
| `pom.xml` (atualizado) | + dependência `org.postgresql:postgresql:42.7.3` |
| `config.ConnectionFactory` | Fábrica de conexões JDBC; lê `application.properties` (ou variáveis `DB_*`) |
| `resources/application.properties.example` | Template de configuração (db.host, db.port, db.name, db.user, db.password) |
| `resources/database/create_tables.sql` | Schema completo: pessoa, cargo, funcionario, medico, quarto, residente, responsavel, residente_responsavel, prontuario, medicamento, registro_clinico, atividade |

## Pré-requisitos

1. PostgreSQL 14+ rodando localmente
2. Banco `cuidar` criado:
   ```sql
   CREATE DATABASE cuidar;
   ```
3. Tabelas criadas a partir do script:
   ```powershell
   psql -U postgres -d cuidar -f src\main\resources\database\create_tables.sql
   ```
4. Copiar o template de configuração e ajustar a senha:
   ```powershell
   Copy-Item src\main\resources\application.properties.example src\main\resources\application.properties
   notepad src\main\resources\application.properties
   ```
   > O `application.properties` real **não** é versionado (ignorado pelo `.gitignore`).

---

# CUIDAR — Versão 1.3 (Modelos clínicos)

Terceira versão. Completa o conjunto dos **12 modelos de domínio** adicionando os modelos clínicos: `Prontuario` (1:1 com Residente), `Medicamento` (catálogo), `RegistroClinico` (evolução de saúde, com `Funcionario`, `Medico` e `Medicamento` opcionais) e `Atividade` (programação semanal com `hora_inicio` + `hora_termino`).

Ainda sem banco, sem UI, sem repositórios — só os modelos compilando.

## Mudanças desde a v1.2

| Novo arquivo | O que representa |
|---|---|
| `model.Prontuario` | Prontuário clínico do residente (peso, altura, tipo sanguíneo, alergias, obs_geral) |
| `model.Medicamento` | Catálogo de medicamentos (nome, fabricante, validade, quantidade em estoque, descrição) |
| `model.RegistroClinico` | Registro de evolução clínica (residente, funcionário, medicamento, médico, tipo evento, intercorrência, data, dosagem) |
| `model.Atividade` | Atividade da agenda semanal (nome, descrição, dia da semana, hora início, hora término) |

`CuidarApp` agora instancia um prontuário, um medicamento, um registro clínico e uma atividade.

---

# CUIDAR — Versão 1.2 (Modelos de moradia)

Segunda versão. Adiciona os **modelos relacionados à moradia do residente** sobre a v1.1: agora temos `Quarto`, `Residente` (com FK para Pessoa e Quarto), `Responsavel` (com endereço próprio) e `ResidenteResponsavel` (associativa com parentesco).

Ainda sem banco de dados, sem UI, sem repositórios — só os modelos compilando.

## Mudanças desde a v1.1

| Novo arquivo | O que representa |
|---|---|
| `model.Quarto` | Quarto da ILPI (número + status) |
| `model.Residente` | Idoso residente (Pessoa + Quarto + status + obs_geral) |
| `model.Responsavel` | Responsável legal (Pessoa + endereço completo + contato) |
| `model.ResidenteResponsavel` | Vínculo Residente↔Responsavel + grau de parentesco |

`CuidarApp` agora também instancia um residente, um responsável e o vínculo entre eles.

---

# CUIDAR — Versão 1.1 (Protótipo de modelos básicos)

Primeira versão do sistema CUIDAR. Apenas as **classes de domínio principais** (Pessoa, Cargo, Funcionario, Medico) com construtores, getters/setters e `toString`. Sem banco de dados, sem UI, sem repositórios — só os modelos compilando e sendo instanciados no `main`.

## O que esta versão tem

- `model.Pessoa` — entidade base (nome, CPF, sexo, data de nascimento, data de cadastro)
- `model.Cargo` — função do funcionário (nome, descrição)
- `model.Funcionario` — composto por Pessoa + Cargo + login/senha + turno + endereço
- `model.Medico` — composto por Pessoa + CRM + especialidade + contato
- `CuidarApp` — `main` que instancia um funcionário e um médico e imprime no console

## Saída esperada

```
=== Sistema CUIDAR ===
Versão 1.1 — Protótipo de modelos básicos

Pessoa{id=1, nomeCompleto='Maria Silva', cpf='111.222.333-44'}
Pessoa{id=2, nomeCompleto='Dr. João Souza', cpf='555.666.777-88'}
Cargo{id=1, nomeCargo='Administrador'}
Funcionario{id=1, pessoa=Pessoa{...}, cargo=Cargo{...}, login='maria.silva'}
Dr. João Souza
```
