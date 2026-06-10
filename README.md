# CUIDAR — Versão 3.6 (PasswordUtil + LoginService com migração + RBAC "Meu Perfil" + Trocar de conta)

Décima quinta versão. **Marco do projeto**: o código-fonte é **byte-idêntico** ao `src/` raiz do repositório — todos os 64 arquivos `.java` batem hash. A partir daqui (v3.7 em diante) começam as features incrementais sobre esta base.

## Mudanças desde a v3.5

### Nova classe: `util/PasswordUtil`

Hash + verificação de senhas com **PBKDF2-HMAC-SHA256**:

- Algoritmo: `PBKDF2WithHmacSHA256`
- Iterações: **120 000**
- Salt: 16 bytes (`SecureRandom`)
- Tamanho da chave derivada: 256 bits
- Formato armazenado: `PBKDF2$<iter>$<saltBase64>$<hashBase64>`
- `isHashed(stored)` — detecta o prefixo `PBKDF2$` para separar senha legada de senha já migrada.
- `verify(plain, stored)` — usa comparação **constant-time** (`byte XOR + OR`) para evitar timing attack.

### `LoginService.autenticar` com migração transparente

```text
1. Busca por login.
2. Se a senha armazenada estiver no formato PBKDF2$… → PasswordUtil.verify (rota normal).
3. Caso contrário (senha legada em texto puro):
   a. Compara em texto puro.
   b. Se bater, gera PasswordUtil.hash(senha) e atualiza no banco (FuncionarioRepository.atualizar).
   c. Retorna o funcionário autenticado.
4. Senão, retorna null.
```

Resultado: senhas antigas migram automaticamente no primeiro login bem-sucedido — sem dropar dados, sem força-bruta.

### `MainFrame`: RBAC "Meu Perfil" + fluxo "Trocar de conta"

O construtor ganhou um sétimo parâmetro `Runnable onLogout` e a sidebar passou a:

| Mudança | Antes | Agora |
|---|---|---|
| Rótulo do item Administrativo | Sempre "Administrativo" | "Administrativo" para `Cargo == Administrador`; **"Meu Perfil"** para os demais |
| Botão "Sair" | `System.exit(0)` direto | `JOptionPane` com três escolhas: **Trocar de conta** / **Encerrar sistema** / **Cancelar**. Em "Trocar de conta", o frame é `dispose()` e o `onLogout` re-abre o `LoginFrame`. |

A ordem dos parâmetros do construtor de `MainFrame` foi reorganizada para casar a do `src/` raiz:

```java
new MainFrame(funcionarioLogado,
              residenteController, funcionarioController, medicoController,
              medicamentoController, prontuarioController,
              registroClinicoController, atividadeController,
              quartoRepository, cargoRepository, onLogout)
```

### `CuidarApp` reescrita

- Sem mais modo `--headless` (era um andaime das versões anteriores; não existe no `src/` raiz).
- Extraído método estático `mostrarLogin(...)` que pode ser re-chamado pelo `onLogout` para reabrir o `LoginFrame`.
- Thread observadora aguarda o `LoginFrame.isVisible() == false`; ao fechar com `funcionarioLogado != null`, abre o `MainFrame` na EDT.

### Reaplicação literal dos demais arquivos

Para garantir bit-identity, 17 arquivos das camadas inferiores foram re-copiados do `src/` raiz (eram cortes de versões anteriores com diferenças apenas de encoding/Javadoc):
`Pessoa`, `Residente`, `RegistroClinico`, 8 `Service`s, `FuncionarioRepository`, 2 `repository.impl`s, `AtividadeController`, `CpfUtil`, `InputHelper`.

## Como rodar (sem fallback headless — abre direto a GUI)

```powershell
$lib = "C:\caminho\para\lib\postgresql-42.7.3.jar"
$files = (Get-ChildItem -Recurse src\main\java -Filter "*.java").FullName
javac -d out -cp $lib $files
Copy-Item src\main\resources\application.properties out\ -ErrorAction SilentlyContinue
java -cp "out;$lib" br.com.cuidar.CuidarApp
```

Para validação offline desta versão, foi usado um *smoke-test* (`SmokeTestV15`) externo à v15 que apenas instancia `PasswordUtil` (roundtrip `hash`/`verify`, `isHashed`) e exercita `LoginService.autenticar` contra o banco real, confirmando que:
- senha errada → `null`
- formato salvo é `PBKDF2$120000$<salt>$<hash>`
- `verify` constant-time bate em hash gerado dinamicamente

## A partir daqui — features incrementais (v3.7 → v3.11)

| Versão | Etapa | Tema |
|---|---|---|
| v3.7 | 16 | Validações UX extras (CPF único reforçado, identificadores imutáveis em edit) |
| v3.8 | 17 | `MeuPerfilPanel` + `LoginService.trocarSenha` |
| v3.9 | 18 | RBAC: campo `quantidade` (estoque) só editável por Administrador |
| v3.10 | 19 | Tabela `atividade_residente` + `DashboardEnfermeiroPanel` (agenda do dia) |
| v3.11 | 20 | Tabela `prescricao_medicamento` + marcar tomada (transação) + ver responsáveis + abas no Dashboard |

---

# CUIDAR — Versão 3.5 (Medicamento + Atividade + Prontuário panels — 5/5 telas)

Décima quarta versão. Acrescentou os **três painéis finais** ao `MainFrame`, fechando as cinco telas da sidebar: Residentes, Medicamentos, Atividades, Prontuário e Administrativo. Não há mais placeholders "em construção".

## Mudanças desde a v3.4

Três novas classes em `br.com.cuidar.view`:

| Nova classe | Responsabilidade |
|---|---|
| `ControleMedicamentoPanel` | Cadastro/edição do **catálogo** de `Medicamento` (Nome, Fabricante, Validade `dd/MM/yyyy`, Quantidade, Descrição). Estados `NOVO/VIEW/EDIT`. Filtro em tempo real por nome via `KeyListener` no `JTextField`. Valida data + número inteiro antes de salvar. |
| `GestaoAtividadePanel` | CRUD completo de `Atividade` (Nome, Dia da Semana — 7 dias, Hora Início + Hora Término `HH:mm`, Descrição). Inclui `JButton` "Excluir" com `JOptionPane` de confirmação. Filtro por dia da semana (combo "Todos / Segunda / .../ Domingo") chama `AtividadeController.listarPorDia(...)`. |
| `ProntuarioPanel` | A tela mais complexa. Topo: busca de `Residente` por CPF (`ResidenteController.buscarPorCpf`). Meio: form de `Prontuario` (Peso, Altura, Tipo Sanguíneo, Alergias, **Obs. Geral**) com estados `VAZIO/NOVO/VIEW/EDIT` — cria automaticamente se o residente ainda não tiver prontuário. Logo abaixo: form de **Novo Registro Clínico** (Tipo, Medicamento, Dosagem, Médico, Intercorrência) que grava com `funcionarioLogado` como autor. Centro: tabela de histórico com botão "Excluir Registro". |

Os três painéis dependem só de controllers/repos já existentes desde a v3.2 — nenhuma camada nova foi necessária.

### Atualizações em `MainFrame`

Construtor passou a receber **oito** colaboradores (todos os 7 controllers + `QuartoRepository` + `CargoRepository`):

```java
new MainFrame(funcionarioLogado,
              residenteController, funcionarioController, medicoController,
              medicamentoController, atividadeController,
              prontuarioController, registroClinicoController,
              quartoRepository, cargoRepository)
```

Os cinco `add(...)` no `CardLayout` agora plugam panels reais:

| Card | Panel |
|---|---|
| `residentes` | `CadastroResidentePanel` |
| `medicamentos` | `ControleMedicamentoPanel` |
| `atividades` | `GestaoAtividadePanel` |
| `prontuario` | `ProntuarioPanel` |
| `administrativo` | `ControleAdministrativoPanel` |

### Atualizações em `CuidarApp`

Monta os **12 repositórios**, **9 serviços** e **7 controladores** completos, e injeta tudo no `MainFrame`. O modo `--headless` agora valida `ProntuarioController.buscarPorResidente(residentes.get(0))` e `RegistroClinicoController.listarPorResidente(...)` além das listagens dos demais.

### O que ainda não tem (chega na v3.6 = "src/" raiz)

- **`util/PasswordUtil`** com PBKDF2-HMAC-SHA256 (210 000 iterações, salt 16 B, formato `PBKDF2$<iter>$<saltB64>$<hashB64>`).
- **`LoginService` com migração automática** de senha legada → PBKDF2 no primeiro login bem-sucedido.
- **RBAC dinâmico na sidebar**: itens visíveis variam por cargo do `funcionarioLogado`; aba "Administrativo" vira "Meu Perfil" para não-administradores (essa parte do `ControleAdministrativoPanel` já está pronta desde a v3.4, mas a sidebar ainda não esconde itens).
- **Trocar de conta**: botão "Sair" abre `JOptionPane` com 3 escolhas ("Trocar de conta / Encerrar sistema / Cancelar") — quando trocar, volta ao `LoginFrame` sem matar o processo.

---

# CUIDAR — Versão 3.4 (CadastroResidentePanel + ControleAdministrativoPanel)

Décima terceira versão. Plugou os dois primeiros painéis funcionais nas abas da sidebar do `MainFrame`: **"Residentes"** ganha o cadastro completo de moradores e **"Administrativo"** ganha o CRUD de funcionários, médicos e quartos (com filtro por cargo do usuário logado).

## Mudanças desde a v3.3

Duas novas classes em `br.com.cuidar.view`:

| Nova classe | Responsabilidade |
|---|---|
| `CadastroResidentePanel` | Formulário (Nome, CPF, Data Nasc., Sexo, Status, Quarto, Observações) + tabela com filtro por nome. Estados `NOVO/VIEW/EDIT`: ao selecionar uma linha, o form vai para `VIEW`; "Editar" libera os campos; "Salvar" persiste via `ResidenteController.cadastrarResidente(...)` ou `editarResidente(...)`. O combo de quartos lista apenas os `Disponível` + o atual do residente sendo editado. Valida CPF com `CpfUtil.isValid()` e máscaras de digitação aplicadas via `InputHelper`. |
| `ControleAdministrativoPanel` | `JTabbedPane` com três abas para administrador (Funcionários, Médicos, Quartos) ou aba única "Meu Perfil" para os demais cargos. Detecta o papel por `funcionarioLogado.getCargo().getNomeCargo()`. Cada aba segue o mesmo ciclo `NOVO/VIEW/EDIT`. A aba Funcionários usa combos com `Cargo` (lista de `CargoRepository`) e `Turno` (Manhã/Tarde/Noite/Diurno/Noturno). Na aba "Meu Perfil", a tabela é só-leitura — qualquer clique em linha de outro funcionário mostra "Acesso restrito". |

Duas novas classes em `br.com.cuidar.util` (eram para entrar só na v3.6, mas os painéis dependem delas):

| Classe | Responsabilidade |
|---|---|
| `CpfUtil` | `isValid(String cpf)` — remove pontuação, rejeita comprimento ≠ 11 e CPFs com todos os dígitos iguais, calcula os dois dígitos verificadores e confere. Implementação clássica. |
| `InputHelper` | Aplica `DocumentFilter` em `JTextField` para máscaras automáticas: `aplicarMascaraData` (dd/MM/yyyy), `aplicarMascaraHora` (HH:mm), `aplicarMascaraCpf`, `aplicarMascaraTelefone` ((00) 00000-0000), `aplicarMascaraCep` (00000-000), além de `aplicarApenasNumeros` e `aplicarApenasDecimal`. |

`PasswordUtil` continua reservada para a **v3.6** (junto da migração de senhas legadas e RBAC) — nesta versão o `LoginService` ainda compara senhas em texto puro, exatamente como na v3.1.

### Atualizações em `MainFrame`

O construtor agora recebe os controllers e repositórios necessários para construir os panels:

```java
new MainFrame(funcionarioLogado, residenteController, funcionarioController,
              medicoController, quartoRepository, cargoRepository)
```

As cinco abas seguem visíveis para todos os usuários (RBAC entra só na v3.6). As três abas restantes — **Medicamentos**, **Atividades** e **Prontuário** — continuam como placeholder "em construção" e serão substituídas na **v3.5**.

### Atualizações em `CuidarApp`

Monta a árvore de dependências (repos → services → controllers) e injeta tudo no `MainFrame`. O modo `--headless` agora exercita `ResidenteController`, `FuncionarioController`, `MedicoController`, `QuartoRepository` e `CargoRepository`, validando que os 5 serviços usados pelos panels respondem contra o banco real.

### O que ainda não tem (chega depois)

- **Painéis Medicamentos / Atividades / Prontuário** — entram na v3.5.
- **PBKDF2 + RBAC + Trocar de conta + Migração de senha legada** — entram na v3.6 junto com `PasswordUtil` e a sincronia final com o `src/` raiz.

---

# CUIDAR — Versão 3.3 (LoginFrame + MainFrame esqueleto)

Décima segunda versão. Primeira aparição da **GUI Swing**: tela de login funcional + frame principal esqueleto (sidebar de navegação com placeholders nas cinco abas).

## Mudanças desde a v3.2

Duas novas classes em `br.com.cuidar.view`:

| Nova classe | Responsabilidade |
|---|---|
| `LoginFrame` | `JFrame` 450×360, GridBagLayout, campos de login/senha, botão "Entrar" (azul `#006699`), `JPasswordField` mascara a senha. Em Enter no campo de senha ou clique no botão, chama `LoginService.autenticar(...)`. Em sucesso, fecha a janela e expõe `getFuncionarioLogado()`. Em falha, mostra `JOptionPane.ERROR_MESSAGE` e limpa a senha. |
| `MainFrame` | `JFrame` 1280×800 (mín. 1024×640), `BorderLayout`. À esquerda, **sidebar** azul com logo CUIDAR, saudação ao usuário ("Olá, &lt;primeiro nome&gt;"), 5 botões de menu (Residentes, Medicamentos, Atividades, Prontuário, Administrativo) e botão "Sair". No centro, um `CardLayout` com placeholders ("&lt;nome da aba&gt; — em construção") — os painéis reais entram em v3.4/v3.5. |

A `CuidarApp` agora tem dois modos:

1. **GUI** (padrão num desktop com display): aplica o L&F do sistema, abre o `LoginFrame` no EDT e, ao receber o `windowClosed` com `getFuncionarioLogado() != null`, abre o `MainFrame`.
2. **Headless** (`--headless` ou `GraphicsEnvironment.isHeadless()`): pula a GUI e exercita o `LoginService` contra o banco real listando os funcionários cadastrados e testando autenticação OK + senha errada. Útil para validar a versão automaticamente sem precisar de display.

### O que ainda não tem (chega depois)

- **Botão Sair só encerra o processo** — a troca de conta com `JOptionPane` ("Trocar de conta / Encerrar sistema / Cancelar") entra na v3.6.
- **Sidebar sem RBAC** — todo usuário vê todos os itens; a aba "Administrativo" vira "Meu Perfil" para não-administradores na v3.6.
- **Painéis vazios** — substituídos por `CadastroResidentePanel`/`ControleAdministrativoPanel` na v3.4 e `ControleMedicamentoPanel`/`GestaoAtividadePanel`/`ProntuarioPanel` na v3.5.

---

# CUIDAR — Versão 3.2 (Camada de Controllers)

Décima primeira versão. Adiciona a **camada de controllers** sobre os 9 services, completando o ciclo Model–Service–Controller antes da chegada da GUI Swing.

## Mudanças desde a v3.1

Sete novas classes em `br.com.cuidar.controller`. Cada controller é uma **fachada fina** que recebe seus services no construtor e delega a operação correspondente. Não há lógica de negócio aqui — apenas a orquestração que será consumida pelos `JPanel`/`JFrame` a partir da v3.3.

| Controller | Service(s) | Operações expostas |
|---|---|---|
| `ResidenteController` | `ResidenteService` + `ResponsavelService` | `cadastrarResidente`, `editarResidente`, `buscarPorCpf`, `buscarPorNome`, `listarTodos`, `vincularResponsavel`, `listarResponsaveis` |
| `FuncionarioController` | `FuncionarioService` | `cadastrarFuncionario`, `editarFuncionario`, `listarTodos` |
| `MedicoController` | `MedicoService` | `cadastrarMedico`, `editarMedico`, `buscarPorCrm`, `listarTodos` |
| `MedicamentoController` | `MedicamentoService` | `cadastrarMedicamento`, `atualizarMedicamento`, `listarTodos` |
| `ProntuarioController` | `ProntuarioService` | `criarProntuario`, `atualizarProntuario`, `buscarPorResidente` |
| `RegistroClinicoController` | `RegistroClinicoService` | `adicionarRegistro`, `excluirRegistro`, `listarPorResidente`, `listarPorPeriodo` |
| `AtividadeController` | `AtividadeService` | `cadastrarAtividade`, `atualizarAtividade`, `excluirAtividade`, `listarTodos`, `listarPorDia` |

> **Por que `ResidenteController` recebe dois services?** Porque a tela de residente também aciona o vínculo com responsáveis — é uma decisão de **UI** (uma só aba) que se reflete na fachada. Mantemos a lógica de vínculo dentro de `ResponsavelService`; o controller apenas delega.

> **Sem `LoginController` nesta versão.** O `LoginFrame` (v3.3) usará o `LoginService` diretamente, já que é uma única tela com uma única operação.

O `CuidarApp` monta a árvore de dependências (repos → services → controllers) e exercita um `listarTodos` em cada controller mais consultas focadas no primeiro residente e um `listarPorDia("Sexta")` na agenda.

---

# CUIDAR — Versão 3.1 (Camada de Serviço)

Décima versão. Introduz a **camada de serviço** sobre os 12 repositórios JDBC, fechando o segundo dos três níveis do MVC (Controller → **Service** → Repository).

## Mudanças desde a v2.6

Nove novos serviços em `br.com.cuidar.service`, cada um recebendo seus repositórios por construtor (injeção manual, sem framework):

| Service | Repositórios usados | Regras concentradas |
|---|---|---|
| `LoginService` | `FuncionarioRepository` | Autenticação por login+senha (texto puro nesta versão — PBKDF2 entra na v3.6) |
| `ResidenteService` | `Residente` + `Pessoa` | Cadastro 2-em-1 (salva Pessoa depois Residente); valida CPF único (RN01); buscar por CPF/nome |
| `FuncionarioService` | `Funcionario` + `Pessoa` | Cadastro 2-em-1; valida CPF único; senha obrigatória |
| `MedicoService` | `Medico` + `Pessoa` | Cadastro 2-em-1; valida CPF único; busca por CRM |
| `MedicamentoService` | `Medicamento` | CRUD do catálogo |
| `ProntuarioService` | `Prontuario` | Criação/atualização e consulta por residente |
| `RegistroClinicoService` | `RegistroClinico` | Adicionar/excluir registros; listar por residente e por período |
| `ResponsavelService` | `Responsavel` + `ResidenteResponsavel` + `Pessoa` | Cadastro de responsável (com Pessoa); vincular/desvincular ao residente; listar por residente |
| `AtividadeService` | `Atividade` | CRUD + filtro por dia da semana |

O `CuidarApp` instancia os 12 repositórios e os 9 services, exercita `listarTodos` em cinco deles, faz um teste de `LoginService.autenticar` com a senha correta do primeiro funcionário e outro com senha errada, e consulta prontuário + registros + vínculos do primeiro residente.

---

# CUIDAR — Versão 2.6 (Repositórios JDBC: Prontuario, Medicamento, RegistroClinico, Atividade)

Nona versão. Fecha o conjunto das **12 implementações JDBC** adicionando as últimas quatro: o prontuário (1:1 com residente), o catálogo de medicamentos, o registro clínico (evento que cruza residente + funcionário + medicamento + médico opcional) e o catálogo de atividades.

## Mudanças desde a v2.5

| Nova classe | Implementa | Particularidade |
|---|---|---|
| `repository.impl.ProntuarioRepositoryImpl` | `ProntuarioRepository` | JOIN em `residente` + `pessoa` + `quarto`; `buscarPorResidente` (1:1); alias `pron_obs` vs `res_obs` para não colidir |
| `repository.impl.MedicamentoRepositoryImpl` | `MedicamentoRepository` | Tabela simples (CATÁLOGO, sem `id_residente`); coluna `quantidade` faz parte do INSERT/UPDATE |
| `repository.impl.RegistroClinicoRepositoryImpl` | `RegistroClinicoRepository` | SQL gigante com 7 JOINs e aliases por entidade (`pres`/`pfunc`/`pmed`); `id_medico` é opcional (`LEFT JOIN` + `setNull(Types.INTEGER)` na inserção); `listarPorPeriodo(LocalDate, LocalDate)` para filtros de relatório |
| `repository.impl.AtividadeRepositoryImpl` | `AtividadeRepository` | Tabela simples com `hora_inicio` + `hora_termino` (LocalTime ↔ `Time.valueOf`); `listarPorDiaSemana(String)` |

Com isso o sistema fica com todas as 12 entidades persistidas. A pasta `repository.impl` chega ao total de 12 classes (Pessoa, Cargo, Quarto, Funcionario, Medico, Residente, Responsavel, ResidenteResponsavel, Prontuario, Medicamento, RegistroClinico, Atividade).

O `CuidarApp` agora imprime o catálogo de medicamentos, a agenda de atividades, e — para o primeiro residente da lista — o prontuário e os registros clínicos em ordem decrescente de data.

---

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
