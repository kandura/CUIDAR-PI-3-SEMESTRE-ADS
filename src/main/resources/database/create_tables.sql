-- =========================================
-- CRIAÇÃO TABELAS - DER CUIDAR (atualizado)
-- =========================================

-- =========================================
-- TABELA: pessoa
-- =========================================

CREATE TABLE pessoa (
    id_pessoa SERIAL PRIMARY KEY,

    nome_completo VARCHAR(150) NOT NULL,
    cpf VARCHAR(14) NOT NULL UNIQUE,
    sexo VARCHAR(20),
    data_nascimento DATE NOT NULL,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =========================================
-- TABELA: quarto
-- =========================================

CREATE TABLE quarto (
    id_quarto SERIAL PRIMARY KEY,

    numero INT NOT NULL UNIQUE,
    status VARCHAR(20) NOT NULL DEFAULT 'Disponível'
    CHECK (status IN (
        'Disponível',
        'Ocupado',
        'Manutenção'
    ))
);

-- =========================================
-- TABELA: cargo
-- =========================================

CREATE TABLE cargo (
    id_cargo SERIAL PRIMARY KEY,
    nome_cargo VARCHAR(100) NOT NULL,
    descricao TEXT
);

-- =========================================
-- TABELA: residente
-- =========================================

CREATE TABLE residente (
    id_residente SERIAL PRIMARY KEY,

    id_quarto INT NOT NULL,
    id_pessoa INT UNIQUE NOT NULL,

    status VARCHAR(30),
    obs_geral TEXT,

    CONSTRAINT fk_residente_quarto
        FOREIGN KEY (id_quarto)
        REFERENCES quarto(id_quarto),

    CONSTRAINT fk_residente_pessoa
        FOREIGN KEY (id_pessoa)
        REFERENCES pessoa(id_pessoa)
);

-- =========================================
-- TABELA: funcionario
-- =========================================

CREATE TABLE funcionario (
    id_funcionario SERIAL PRIMARY KEY,

    id_pessoa INT UNIQUE NOT NULL,
    id_cargo INT NOT NULL,

    login VARCHAR(50) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    turno VARCHAR(20),
    telefone VARCHAR(20),
    email VARCHAR(100),
    rua VARCHAR(150),
    numero INT,
    cep VARCHAR(10),

    CONSTRAINT fk_funcionario_pessoa
        FOREIGN KEY (id_pessoa)
        REFERENCES pessoa(id_pessoa),

    CONSTRAINT fk_funcionario_cargo
        FOREIGN KEY (id_cargo)
        REFERENCES cargo(id_cargo)
);

-- =========================================
-- TABELA: responsavel
-- =========================================

CREATE TABLE responsavel (
    id_responsavel SERIAL PRIMARY KEY,

    id_pessoa INT UNIQUE NOT NULL,

    telefone VARCHAR(20),
    email VARCHAR(100),
    rua VARCHAR(150),
    numero INT,
    bairro VARCHAR(100),
    cidade VARCHAR(100),
    estado VARCHAR(50),
    cep VARCHAR(10),

    CONSTRAINT fk_responsavel_pessoa
        FOREIGN KEY (id_pessoa)
        REFERENCES pessoa(id_pessoa)
);

-- =========================================
-- TABELA: residente_responsavel
-- =========================================

CREATE TABLE residente_responsavel (
    id_residente_responsavel SERIAL PRIMARY KEY,

    id_residente INT NOT NULL,
    id_responsavel INT NOT NULL,

    parentesco VARCHAR(50),

    CONSTRAINT fk_rr_residente
        FOREIGN KEY (id_residente)
        REFERENCES residente(id_residente),

    CONSTRAINT fk_rr_responsavel
        FOREIGN KEY (id_responsavel)
        REFERENCES responsavel(id_responsavel)
);

-- =========================================
-- TABELA: prontuario
-- =========================================

CREATE TABLE prontuario (
    id_prontuario SERIAL PRIMARY KEY,

    id_residente INT UNIQUE NOT NULL,

    peso NUMERIC(5,2),
    altura NUMERIC(3,2),
    tipo_sanguineo VARCHAR(5),
    alergias TEXT,
    obs_geral TEXT,

    CONSTRAINT fk_prontuario_residente
        FOREIGN KEY (id_residente)
        REFERENCES residente(id_residente)
);

-- =========================================
-- TABELA: medicamento
-- =========================================

CREATE TABLE medicamento (
    id_medicamento SERIAL PRIMARY KEY,

    nome VARCHAR(150) NOT NULL,
    fabricante VARCHAR(100),
    data_validade DATE,
    quantidade INT,
    descricao TEXT
);

-- =========================================
-- TABELA: medico
-- =========================================

CREATE TABLE medico (
    id_medico SERIAL PRIMARY KEY,

    id_pessoa INT UNIQUE NOT NULL,

    crm VARCHAR(20) NOT NULL UNIQUE,
    especialidade VARCHAR(100),
    telefone VARCHAR(20),
    email VARCHAR(100),

    CONSTRAINT fk_medico_pessoa
        FOREIGN KEY (id_pessoa)
        REFERENCES pessoa(id_pessoa)
);

-- =========================================
-- TABELA: registro_clinico
-- =========================================

CREATE TABLE registro_clinico (
    id_registro_clinico SERIAL PRIMARY KEY,

    id_residente INT NOT NULL,
    id_funcionario INT NOT NULL,
    id_medicamento INT,
    id_medico INT,

    tipo_evento VARCHAR(100),
    intercorrencia TEXT,
    data_registro DATE DEFAULT CURRENT_DATE,
    dosagem VARCHAR(50),

    CONSTRAINT fk_registro_residente
        FOREIGN KEY (id_residente)
        REFERENCES residente(id_residente),

    CONSTRAINT fk_registro_funcionario
        FOREIGN KEY (id_funcionario)
        REFERENCES funcionario(id_funcionario),

    CONSTRAINT fk_registro_medicamento
        FOREIGN KEY (id_medicamento)
        REFERENCES medicamento(id_medicamento),

    CONSTRAINT fk_registro_medico
        FOREIGN KEY (id_medico)
        REFERENCES medico(id_medico)
);

-- =========================================
-- TABELA: atividade
-- =========================================

CREATE TABLE atividade (
    id_atividade SERIAL PRIMARY KEY,

    nome VARCHAR(100) NOT NULL,
    descricao TEXT,
    dia_semana VARCHAR(20) NOT NULL,
    hora_inicio TIME NOT NULL,
    hora_termino TIME NOT NULL
);
