# Escopo do Sistema – CUIDAR

## 1. Apresentação do Projeto

O sistema CUIDAR é um software desktop desenvolvido como Projeto Integrador do 3º semestre do curso de Análise e Desenvolvimento de Sistemas. Seu propósito é apoiar a gestão de informações de residentes de uma Instituição de Longa Permanência para Idosos (ILPI), centralizando dados importantes em um ambiente organizado, seguro e de fácil consulta.

A proposta do sistema nasce da necessidade de reduzir a dependência de registros dispersos, anotações manuais e controles informais, que dificultam a visualização rápida das informações e aumentam o risco de falhas no acompanhamento do residente. Dessa forma, o CUIDAR busca oferecer uma estrutura única para cadastro, acompanhamento clínico, controle de medicamentos, registro de rotinas e consulta de informações relevantes para a equipe da instituição.

Este documento define o escopo do sistema, isto é, delimita com clareza o que o projeto pretende entregar, quais problemas ele busca resolver, quais funcionalidades fazem parte da proposta atual e quais pontos ficam fora da primeira versão. Essa definição é essencial para evitar crescimento desordenado do projeto, desalinhamento entre os integrantes do grupo e expectativas irreais durante o desenvolvimento.

---

## 2. Objetivo Geral

Desenvolver um sistema desktop para gerenciamento das informações dos residentes de uma ILPI, permitindo o cadastro, a atualização, a consulta e o acompanhamento de dados pessoais, clínicos e operacionais, de modo a melhorar a organização interna, a segurança das informações e a rastreabilidade do cuidado prestado.

---

## 3. Objetivos Específicos

Os objetivos específicos do sistema CUIDAR são:

- centralizar as informações dos residentes em um único sistema;
- facilitar o acesso aos dados cadastrais e clínicos;
- permitir o registro organizado de informações médicas permanentes e registros clínicos históricos;
- controlar os medicamentos vinculados a cada residente;
- registrar rotinas e cuidados diários;
- permitir a identificação dos diferentes tipos de usuários do sistema;
- melhorar a visualização das informações por meio de telas e relatórios simples;
- reduzir erros de duplicidade, perda de informação e falta de padronização nos registros;
- oferecer uma base sólida para futuras expansões do sistema.

---

## 4. Problema que o Sistema Busca Resolver

Em instituições que realizam cuidados contínuos de idosos, é comum que existam muitas informações importantes para serem registradas e acompanhadas diariamente. Dados pessoais, doenças crônicas, alergias, histórico de intercorrências, medicamentos em uso, alimentação, higiene e outros cuidados precisam estar disponíveis de forma clara e confiável para os profissionais responsáveis.

Quando essas informações estão espalhadas em papéis, planilhas isoladas ou registros não padronizados, surgem diversos problemas, tais como:

- dificuldade para localizar informações rapidamente;
- risco de perda de dados importantes;
- possibilidade de registros duplicados ou inconsistentes;
- falhas na comunicação entre os profissionais;
- baixa rastreabilidade sobre o que foi registrado, por quem e quando;
- dificuldade para acompanhar a evolução do residente ao longo do tempo;
- mistura de informações clínicas com informações operacionais do dia a dia.

O sistema CUIDAR busca resolver esse cenário por meio de uma estrutura organizada em módulos, com foco no residente como elemento central do sistema.

---

## 5. Público-Alvo e Usuários do Sistema

O sistema é voltado para instituições que necessitam registrar e consultar informações de residentes de forma segura e organizada, especialmente ILPIs.

Os principais usuários do sistema são:

### 5.1 Administrador
Usuário com acesso mais amplo ao sistema. Pode gerenciar cadastros, acompanhar informações e administrar módulos de uso interno.

### 5.2 Enfermeiro
Usuário com papel ligado ao acompanhamento clínico e ao controle de informações de saúde do residente, incluindo prontuário, registros clínicos e medicamentos.

### 5.3 Cuidador
Usuário com foco no registro das rotinas diárias, observações gerais e consulta de informações básicas necessárias para o cuidado.

Cada perfil possui responsabilidades diferentes dentro do sistema, o que justifica a necessidade de separação de permissões e controle de acesso.

---

## 6. Escopo Funcional do Sistema

Esta seção descreve o que efetivamente faz parte do sistema na proposta atual.

### 6.1 Módulo de Residentes
O sistema deverá permitir o gerenciamento completo dos residentes da instituição, considerando esse módulo como núcleo central de toda a aplicação.

Estão incluídas neste módulo as seguintes funcionalidades:

- cadastro de residentes;
- edição de dados cadastrais;
- inativação de residentes em vez de exclusão definitiva;
- listagem de residentes;
- busca por informações como nome e CPF;
- visualização detalhada do cadastro.

Também fazem parte do escopo os principais dados do residente, tais como:

- nome completo;
- data de nascimento;
- CPF;
- sexo;
- contato de familiar ou responsável;
- data de entrada na instituição;
- observações gerais.

Este módulo é o ponto de ligação dos demais, pois prontuários, registros clínicos, medicamentos e rotinas devem estar associados diretamente a um residente.

### 6.2 Módulo de Prontuário
O sistema deverá permitir a criação e a manutenção do prontuário do residente, contendo informações médicas permanentes e relevantes para o acompanhamento geral da saúde.

Estão incluídos neste módulo:

- criação de prontuário vinculado ao residente;
- registro de doenças crônicas;
- registro de alergias;
- registro do tipo sanguíneo;
- registro de condições médicas relevantes.

O prontuário representa informações mais estáveis e não deve funcionar como um histórico de ocorrências diárias. Sua função é concentrar informações fixas ou de longo prazo que precisam estar acessíveis em qualquer atendimento.

### 6.3 Módulo de Registros Clínicos
O sistema deverá permitir o acompanhamento clínico contínuo por meio de registros cronológicos adicionados ao longo do tempo.

Estão incluídas neste módulo as seguintes funcionalidades:

- registro de evoluções clínicas;
- registro de observações médicas;
- registro de intercorrências, como quedas, febre ou outros eventos;
- visualização do histórico clínico em ordem cronológica.

Esse módulo deve funcionar de maneira histórica, isto é, os registros não devem substituir informações anteriores, mas sim acrescentar novas ocorrências ao histórico do residente.

### 6.4 Módulo de Medicamentos
O sistema deverá permitir o registro e a consulta de medicamentos vinculados ao residente.

Estão incluídas neste módulo as seguintes funcionalidades:

- cadastro de medicamentos para um residente;
- definição do nome do medicamento;
- definição da dosagem;
- definição da frequência;
- definição de horários.

Esse módulo tem como foco o controle do que cada residente utiliza, não sendo o objetivo do sistema funcionar como um controle geral de estoque farmacêutico institucional.

### 6.5 Módulo de Rotinas e Cuidados
O sistema deverá permitir o registro de atividades operacionais do dia a dia relacionadas ao cuidado do residente.

Estão incluídas neste módulo as seguintes funcionalidades:

- registro de alimentação;
- registro de banho;
- registro de higiene;
- registro de observações gerais;
- registro por data.

Esse módulo é diferente do prontuário e dos registros clínicos, pois trata do acompanhamento operacional da rotina diária e não das informações médicas permanentes ou da evolução clínica formal.

### 6.6 Módulo de Funcionários
O sistema deverá permitir o cadastro básico dos funcionários que utilizam o software.

Estão incluídas neste módulo as seguintes funcionalidades:

- cadastro de funcionário;
- identificação do tipo de usuário;
- associação do funcionário a um perfil de acesso.

Os perfis inicialmente previstos são:

- administrador;
- enfermeiro;
- cuidador.

O sistema de autenticação pode ser simplificado na primeira versão, desde que a estrutura de perfis e responsabilidades esteja prevista.

### 6.7 Módulo de Relatórios e Consultas
O sistema deverá oferecer meios de visualização organizada das informações, mesmo que inicialmente em formato simples, sem necessidade de exportação avançada.

Estão incluídas neste módulo as seguintes funcionalidades:

- listagem de residentes;
- visualização de prontuários;
- visualização de medicamentos ativos;
- visualização de histórico clínico;
- consultas gerais para apoio à operação interna.

Na primeira versão, esse módulo pode ser implementado por meio de telas específicas de consulta, sem necessidade de geração de arquivos em PDF, planilhas ou dashboards avançados.

### 6.8 Módulo de Interface
O sistema deverá possuir telas que representem os principais módulos e permitam navegação clara entre as funcionalidades.

Estão incluídas neste módulo as seguintes telas ou equivalentes:

- tela inicial;
- tela de listagem de residentes;
- tela de cadastro de residente;
- tela de prontuário;
- tela de registros clínicos;
- tela de medicamentos;
- tela de rotinas e cuidados;
- tela de funcionários;
- telas de consulta e visualização.

A interface deve ser planejada de modo a refletir a estrutura lógica do sistema e facilitar o uso por pessoas com diferentes níveis de familiaridade tecnológica.

---

## 7. Escopo de Dados

O sistema deverá armazenar, no mínimo, os seguintes grupos de dados:

### 7.1 Dados cadastrais do residente
- nome;
- data de nascimento;
- CPF;
- sexo;
- contato de responsável;
- data de entrada;
- observações.

### 7.2 Dados clínicos fixos
- doenças crônicas;
- alergias;
- tipo sanguíneo;
- condições médicas relevantes.

### 7.3 Dados clínicos históricos
- data e hora do registro;
- descrição da evolução ou evento;
- identificação do profissional responsável.

### 7.4 Dados de medicamentos
- nome do medicamento;
- dosagem;
- frequência;
- horários;
- vínculo com o residente.

### 7.5 Dados de rotinas
- tipo de cuidado realizado;
- data;
- observação complementar;
- profissional responsável, quando aplicável.

### 7.6 Dados de funcionários
- nome;
- tipo de usuário;
- informações básicas de identificação necessárias ao sistema.

---

## 8. Regras Gerais Incluídas no Escopo

Fazem parte do escopo do sistema as seguintes diretrizes gerais de funcionamento:

- o residente é a entidade central do sistema;
- os demais módulos dependem do residente;
- o CPF do residente deve ser único;
- o prontuário deve existir como estrutura vinculada ao residente;
- registros clínicos devem compor um histórico e não sobrescrever informações;
- medicamentos devem estar sempre vinculados a um residente;
- registros de rotina não devem ser confundidos com registros clínicos;
- o sistema deve prever perfis de acesso distintos;
- relatórios e consultas devem apenas visualizar dados, não alterá-los;
- residentes não devem ser excluídos definitivamente na lógica de negócio principal, mas inativados.

---

## 9. Itens Fora do Escopo da Primeira Versão

Esta seção é fundamental para delimitar o projeto e evitar que o grupo tente construir mais do que consegue entregar com qualidade.

Na primeira versão do sistema, ficam fora do escopo:

- aplicativo mobile;
- versão web;
- integração com sistemas hospitalares externos;
- integração com dispositivos médicos;
- controle completo de estoque de farmácia;
- emissão de receitas;
- assinatura digital de documentos;
- exportação avançada de relatórios em múltiplos formatos;
- dashboards analíticos complexos;
- envio automático de notificações por e-mail, SMS ou aplicativos de mensagem;
- integração com serviços em nuvem;
- biometria;
- reconhecimento facial;
- permissões extremamente detalhadas por tela e operação;
- módulo financeiro;
- módulo de folha de pagamento;
- agendamento completo de consultas externas;
- controle de ambulâncias, transporte ou logística externa;
- prontuário eletrônico com validade jurídica equivalente a sistemas hospitalares especializados.

Esses itens podem ser considerados em evoluções futuras, mas não fazem parte do escopo inicial do PI.

---

## 10. Limites do Projeto

O sistema CUIDAR não pretende substituir sistemas hospitalares de grande porte nem cobrir todas as necessidades administrativas de uma instituição de saúde. Seu foco está na gestão organizada das informações dos residentes dentro do contexto proposto para o Projeto Integrador.

Isso significa que o projeto terá limites claros:

- não abrangerá todos os setores possíveis de uma instituição;
- não incluirá recursos altamente complexos que exijam infraestrutura avançada;
- priorizará módulos essenciais e funcionais;
- buscará estabilidade, clareza e coerência em vez de excesso de funcionalidades.

Esses limites são intencionais e fazem parte de uma definição de escopo madura.

---

## 11. Benefícios Esperados

Com a implementação do sistema CUIDAR, espera-se alcançar os seguintes benefícios:

- centralização das informações dos residentes;
- maior organização dos registros;
- redução de duplicidade e inconsistência de dados;
- melhor rastreabilidade das informações;
- facilidade de consulta por parte dos profissionais;
- separação mais clara entre informações clínicas, cadastrais e operacionais;
- suporte à rotina institucional;
- base estruturada para futuras melhorias do sistema.

---

## 12. Critérios Gerais de Sucesso do Escopo

Pode-se considerar que o escopo inicial foi atendido se o sistema conseguir:

- cadastrar, editar, listar, buscar e inativar residentes;
- manter prontuário vinculado ao residente;
- registrar histórico clínico sem sobrescrever dados anteriores;
- cadastrar medicamentos vinculados a residentes;
- registrar rotinas e cuidados diários;
- cadastrar funcionários e distinguir perfis de uso;
- apresentar telas funcionais para navegação e consulta;
- fornecer consultas e visualizações organizadas dos dados;
- manter coerência entre regras de negócio, módulos e estrutura do sistema.

---

## 13. Considerações Finais

O escopo do sistema CUIDAR foi definido com foco em viabilidade, clareza e coerência com a proposta acadêmica do Projeto Integrador. A escolha por um sistema modular, centrado no residente, permite que o desenvolvimento seja conduzido de forma organizada e progressiva, começando pelos elementos centrais e avançando para os complementares.

Ao delimitar com precisão o que está incluído e o que está fora da primeira versão, o grupo reduz riscos de retrabalho, evita crescimento descontrolado do projeto e aumenta as chances de entregar uma solução consistente, compreensível e bem estruturada.

Este documento deve servir como referência durante todo o desenvolvimento, apoiando decisões de modelagem, implementação, interface e priorização de funcionalidades.
