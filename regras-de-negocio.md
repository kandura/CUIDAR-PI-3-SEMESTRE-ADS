# 📘 Regras de Negócio – Sistema CUIDAR

Este documento define as **Regras de Negócio** do sistema CUIDAR, estabelecendo normas, restrições e comportamentos que garantem a integridade dos dados e a segurança dos residentes.

As regras transformam os requisitos funcionais em diretrizes operacionais, assegurando que o sistema:
- previna erros humanos
- mantenha rastreabilidade histórica
- respeite hierarquias de acesso entre usuários

---

# 🧓 Módulo de Residentes (O Coração do Sistema)

## RN01 – Unicidade de Identificação
O sistema não deve permitir o cadastro de dois residentes com o mesmo CPF.

**Justificativa:**  
O CPF é o identificador único legal. Evitar duplicidade impede mistura de históricos clínicos, reduzindo riscos à saúde.

## RN02 – Inativação em vez de Exclusão (Soft Delete)
Um residente nunca deve ser excluído, apenas marcado como **Inativo**.

**Justificativa:**  
A legislação exige manutenção do histórico. A exclusão comprometeria a rastreabilidade.

## RN03 – Idade Mínima e Responsável
Se o residente for menor ou incapaz, deve haver pelo menos um contato responsável cadastrado.

**Justificativa:**  
Garante respaldo legal para decisões e emergências.

---

# 📋 Módulo de Prontuário (Informações Fixas)

## RN04 – Vínculo Vitalício
O prontuário é criado na admissão e não pode ser excluído.

**Justificativa:**  
Contém dados críticos (ex: tipo sanguíneo, alergias). Sua ausência compromete atendimentos.

## RN05 – Alerta de Alergias
O sistema deve exibir alertas visuais para alergias em todas as telas do residente.

**Justificativa:**  
Previne administração de medicamentos inadequados.

---

# 🏥 Módulo de Registros Clínicos (Histórico)

## RN06 – Imutabilidade do Registro (Append-only)
Registros não podem ser editados ou excluídos. Correções devem ser feitas via retificação.

**Justificativa:**  
Garante validade jurídica e auditoria.

## RN07 – Carimbo de Tempo e Autoria
Todo registro deve conter automaticamente:
- data/hora
- ID do funcionário

**Justificativa:**  
Permite rastrear quem fez cada ação.

---

# 💊 Módulo de Medicamentos

## RN08 – Bloqueio de Medicamento Órfão
Não é permitido cadastrar medicamento sem vínculo com residente.

**Justificativa:**  
Evita confusão e mantém foco no cuidado individual.

## RN09 – Status de Atividade
Medicamentos devem possuir status:
- Ativo
- Suspenso

**Justificativa:**  
Permite acompanhar tratamentos atuais e históricos.

---

# 🛁 Módulo de Rotinas e Cuidados

## RN10 – Diferenciação de Escopo
Registros de rotina não devem ser misturados com prontuário médico.

**Justificativa:**  
Evita poluição de dados clínicos com informações operacionais.

## RN11 – Fechamento Diário
O sistema deve indicar se todas as rotinas do dia foram preenchidas.

**Justificativa:**  
Garante qualidade do cuidado e evita falhas na assistência.

---

# 🔐 Módulo de Funcionários e Segurança

## RN12 – Controle de Acesso por Perfil (RBAC)

- **Administrador:** acesso total  
- **Enfermeiro:** edição de prontuário e medicamentos  
- **Cuidador:** registro de rotinas e visualização básica  

**Justificativa:**  
Atende princípios de segurança e LGPD.

## RN13 – Log de Operações Críticas
Alterações em dados sensíveis devem ser registradas em log.

**Justificativa:**  
Permite auditoria e identificação de erros.

---

# 📊 Módulo de Relatórios

## RN14 – Consistência de Dados
Relatórios devem apenas ler dados, sem modificá-los.

**Justificativa:**  
Garante integridade das informações e segurança do sistema.

---

# 📌 Observação Final
Estas regras garantem que o sistema CUIDAR opere com segurança, confiabilidade e conformidade com boas práticas da área da saúde.
