# Módulos e Funcionalidades – Sistema CUIDAR

## Visão Geral

O sistema CUIDAR será estruturado em módulos independentes, porém interligados, tendo como elemento central o **residente**.

Todos os demais módulos (prontuário, medicamentos, rotinas, etc.) estarão diretamente associados a um residente, garantindo:
- organização
- rastreabilidade
- consistência dos dados

---

# 1. Módulo de Residentes (NÚCLEO DO SISTEMA)

Responsável pelo gerenciamento completo dos residentes da instituição.

## Funcionalidades
- Cadastro de residente  
- Edição de dados  
- Inativação/remoção  
- Listagem de residentes  
- Busca por nome, CPF, etc.  
- Visualização detalhada  

## Dados principais
- Nome completo  
- Data de nascimento  
- CPF  
- Sexo  
- Contato de responsável  
- Data de entrada  
- Observações  

## Regra
Todos os outros módulos dependem diretamente do residente.

---

# 2. Módulo de Prontuário (INFORMAÇÕES FIXAS)

Responsável por armazenar informações médicas permanentes.

## Funcionalidades
- Criação de prontuário  
- Registro de informações clínicas fixas  

## Dados principais
- Doenças crônicas  
- Alergias  
- Tipo sanguíneo  
- Condições médicas  

## Regra
Essas informações não devem ser sobrescritas constantemente.

---

# 3. Módulo de Registros Clínicos (HISTÓRICO)

Responsável pelo acompanhamento da saúde.

## Funcionalidades
- Registro de evoluções clínicas  
- Observações médicas  
- Registro de eventos  
- Histórico cronológico  

## Estrutura
Um residente → vários registros clínicos

## Regra
Nunca sobrescrever dados — sempre adicionar novos registros.

---

# 4. Módulo de Medicamentos

Responsável pelo controle de medicamentos.

## Funcionalidades
- Cadastro de medicamento por residente  
- Definição de:
  - Nome  
  - Dosagem  
  - Frequência  
  - Horários  

## Regra
Medicamentos sempre vinculados a um residente.

---

# 5. Módulo de Rotinas e Cuidados

Responsável pelas atividades do dia a dia.

## Funcionalidades
- Registro de:
  - Alimentação  
  - Banho  
  - Higiene  
  - Observações  
- Registro por data  

## Regra
Rotina ≠ Prontuário  
- Rotina → dia a dia  
- Prontuário → médico  

---

# 6. Módulo de Funcionários

Responsável pelos usuários do sistema.

## Funcionalidades
- Cadastro de funcionário  
- (Opcional) login  
- Tipos:
  - Administrador  
  - Cuidador  
  - Enfermeiro  

---

# 7. Módulo de Relatórios

Responsável pela visualização dos dados.

## Funcionalidades
- Listagem de residentes  
- Visualização de prontuários  
- Medicamentos ativos  
- Histórico clínico  

---

# 8. Módulo de Interface (Telas)

Responsável pela interação do usuário.

## Telas principais
- Dashboard  
- Lista de residentes  
- Cadastro  
- Prontuário  
- Registros clínicos  
- Medicamentos  
- Funcionários (opcional)  

---

# Relação entre os Módulos

Estrutura central:

Residente  
↓  
Prontuário  
↓  
Registros Clínicos  
↓  
Medicamentos  

Paralelo:
- Funcionários → operam o sistema  
- Rotinas → dia a dia  

---

# Ordem de Implementação

1. Residentes (CRUD completo)  
2. Prontuário  
3. Registros clínicos  
4. Medicamentos  
5. Rotinas  
6. Funcionários  
7. Relatórios  
