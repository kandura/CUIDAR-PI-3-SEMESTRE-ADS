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

## Como rodar

Sem Maven (com o driver baixado em `lib/postgresql-42.7.3.jar`):

```powershell
$lib = "C:\caminho\para\lib\postgresql-42.7.3.jar"
$files = (Get-ChildItem -Recurse src\main\java -Filter "*.java").FullName
javac -d out $files
Copy-Item src\main\resources\application.properties out\ -ErrorAction SilentlyContinue
java -cp "out;$lib" br.com.cuidar.CuidarApp
```

Com Maven:

```powershell
mvn -q compile exec:java -Dexec.mainClass="br.com.cuidar.CuidarApp"
```

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
