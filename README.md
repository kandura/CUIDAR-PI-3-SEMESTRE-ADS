# CUIDAR — Versão 1.1 (Protótipo de modelos básicos)

Primeira versão do sistema CUIDAR. Apenas as **classes de domínio principais** (Pessoa, Cargo, Funcionario, Medico) com construtores, getters/setters e `toString`. Sem banco de dados, sem UI, sem repositórios — só os modelos compilando e sendo instanciados no `main`.

## O que esta versão tem

- `model.Pessoa` — entidade base (nome, CPF, sexo, data de nascimento, data de cadastro)
- `model.Cargo` — função do funcionário (nome, descrição)
- `model.Funcionario` — composto por Pessoa + Cargo + login/senha + turno + endereço
- `model.Medico` — composto por Pessoa + CRM + especialidade + contato
- `CuidarApp` — `main` que instancia um funcionário e um médico e imprime no console

## Como rodar

### Opção A — javac/java direto

```powershell
# Compilar
$files = (Get-ChildItem -Recurse src\main\java -Filter "*.java").FullName
javac -d out $files

# Rodar
java -cp out br.com.cuidar.CuidarApp
```

### Opção B — Maven

```powershell
mvn -q compile exec:java -Dexec.mainClass="br.com.cuidar.CuidarApp"
```

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

## Próxima versão

`v02-pessoa-cargo-medico` (1.2) — adiciona os modelos de moradia: Quarto, Residente, Responsavel, ResidenteResponsavel.
