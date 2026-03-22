# Roadmap de Desenvolvimento – Sistema CUIDAR

## 1. Visão Geral

Este documento define o planejamento de desenvolvimento do sistema CUIDAR, estabelecendo uma sequência lógica de implementação baseada na dependência entre os módulos.

O objetivo do roadmap é organizar o trabalho do grupo, evitando retrabalho, reduzindo complexidade e garantindo que cada etapa seja construída sobre uma base sólida.

A estratégia adotada segue o princípio de desenvolvimento incremental, iniciando pelo núcleo do sistema e avançando gradualmente para os módulos complementares.

---

## 2. Estratégia de Desenvolvimento

O desenvolvimento será realizado em fases, considerando:

- dependência entre módulos;
- complexidade técnica;
- importância funcional;
- necessidade de validação progressiva.

A prioridade será sempre implementar primeiro os elementos estruturais do sistema, garantindo que os dados estejam organizados antes da construção de funcionalidades mais avançadas.

---

## 3. Fases de Desenvolvimento

### Fase 1 – Módulo de Residentes (Base do Sistema)

Objetivo:  
Criar o núcleo central do sistema, responsável por sustentar todos os demais módulos.

Funcionalidades:

- cadastro de residente;
- edição de dados;
- inativação de residente;
- listagem de residentes;
- busca por nome e CPF;
- visualização detalhada.

Importância:  
Todos os outros módulos dependem diretamente do residente.

Resultado esperado:

- estrutura de dados definida;
- base funcional estável;
- CRUD completo implementado.

---

### Fase 2 – Módulo de Prontuário

Objetivo:  
Permitir o registro de informações médicas permanentes.

Funcionalidades:

- criação de prontuário vinculado ao residente;
- registro de doenças crônicas;
- registro de alergias;
- registro de tipo sanguíneo;
- registro de condições médicas relevantes.

Importância:  
Define o estado clínico base do residente.

Resultado esperado:

- prontuário estruturado;
- vínculo consistente com residente.

---

### Fase 3 – Módulo de Registros Clínicos

Objetivo:  
Registrar o histórico de evolução clínica.

Funcionalidades:

- registro de evoluções clínicas;
- observações;
- eventos;
- histórico cronológico.

Resultado esperado:

- histórico funcional;
- rastreabilidade garantida.

---

### Fase 4 – Módulo de Medicamentos

Objetivo:  
Controlar medicamentos.

Funcionalidades:

- cadastro de medicamentos;
- dosagem;
- frequência;
- horários.

Resultado esperado:

- controle básico implementado;
- vínculo correto com residentes.

---

### Fase 5 – Módulo de Rotinas

Objetivo:  
Registrar atividades do dia a dia.

Funcionalidades:

- alimentação;
- banho;
- higiene;
- observações.

Resultado esperado:

- registros diários organizados.

---

### Fase 6 – Módulo de Funcionários

Objetivo:  
Gerenciar usuários.

Funcionalidades:

- cadastro;
- definição de perfil.

Resultado esperado:

- estrutura de usuários definida.

---

### Fase 7 – Módulo de Relatórios

Objetivo:  
Visualizar informações.

Funcionalidades:

- listagens;
- consultas;
- visualizações.

Resultado esperado:

- leitura organizada dos dados.

---

## 4. Ordem de Prioridade

1. Residentes  
2. Prontuário  
3. Registros Clínicos  
4. Medicamentos  
5. Rotinas  
6. Funcionários  
7. Relatórios  

---

## 5. Critérios de Conclusão

Uma fase é concluída quando:

- funcionalidades implementadas;
- sem erros críticos;
- funcionamento validado;
- entendimento do grupo.

---

## 6. Considerações Finais

Este roadmap orienta o desenvolvimento do sistema CUIDAR de forma estruturada e progressiva, garantindo qualidade e coerência ao longo do projeto.
