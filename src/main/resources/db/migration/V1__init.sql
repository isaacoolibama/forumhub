-- =====================================================
-- Todas as tabelas e dados iniciais
-- =====================================================

-- Tabela de perfis (roles)
CREATE TABLE IF NOT EXISTS perfis (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(50) NOT NULL UNIQUE
);

-- Tabela de usuários
CREATE TABLE IF NOT EXISTS usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabela de relacionamento usuário-perfil (muitos para muitos)
CREATE TABLE IF NOT EXISTS usuarios_perfis (
    usuario_id BIGINT NOT NULL,
    perfil_id BIGINT NOT NULL,
    PRIMARY KEY (usuario_id, perfil_id),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    FOREIGN KEY (perfil_id) REFERENCES perfis(id) ON DELETE CASCADE
);

-- Tabela de cursos
CREATE TABLE IF NOT EXISTS cursos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    categoria VARCHAR(50) NOT NULL
);

-- Tabela de tópicos
CREATE TABLE IF NOT EXISTS topicos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    mensagem TEXT NOT NULL,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status ENUM('ABERTO', 'FECHADO', 'RESOLVIDO') DEFAULT 'ABERTO',
    autor_id BIGINT NOT NULL,
    curso_id BIGINT NOT NULL,
    FOREIGN KEY (autor_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    FOREIGN KEY (curso_id) REFERENCES cursos(id) ON DELETE CASCADE
);

-- Tabela de respostas
CREATE TABLE IF NOT EXISTS respostas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    mensagem TEXT NOT NULL,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    autor_id BIGINT NOT NULL,
    topico_id BIGINT NOT NULL,
    solucao BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (autor_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    FOREIGN KEY (topico_id) REFERENCES topicos(id) ON DELETE CASCADE
);

-- =====================================================
-- DADOS INICIAIS
-- =====================================================

-- Inserir perfis padrão
INSERT INTO perfis (nome) VALUES ('ROLE_USUARIO');
INSERT INTO perfis (nome) VALUES ('ROLE_ADMIN');

-- Inserir cursos de teste
INSERT INTO cursos (nome, categoria) VALUES 
('Java Básico', 'Programação'),
('Spring Boot', 'Framework'),
('MySQL', 'Banco de Dados'),
('API REST', 'Desenvolvimento Web'),
('Git e GitHub', 'Controle de Versão');

-- Inserir usuário de teste para tópicos (será criado via API, mas precisamos para foreign key)
INSERT INTO usuarios (nome, email, senha, data_criacao) VALUES 
('Usuario Teste', 'usuario@teste.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', NOW());

-- Associar perfil USUARIO ao usuário de teste
INSERT INTO usuarios_perfis (usuario_id, perfil_id) 
SELECT u.id, p.id FROM usuarios u, perfis p 
WHERE u.email = 'usuario@teste.com' AND p.nome = 'ROLE_USUARIO';

-- Inserir 5 tópicos de teste do mesmo usuário
INSERT INTO topicos (titulo, mensagem, status, autor_id, curso_id, data_criacao) VALUES 
('Como começar com Java?', 'Estou iniciando meus estudos em Java. Alguém pode me dar algumas dicas de como começar? Quais são os primeiros passos?', 'ABERTO', 1, 1, NOW()),
('Problema com Spring Boot', 'Minha aplicação Spring Boot não está iniciando. Aparece um erro de conexão com o banco. Alguém pode ajudar?', 'ABERTO', 1, 2, NOW()),
('Dúvida sobre MySQL', 'Qual a diferença entre INNER JOIN e LEFT JOIN? Quando usar cada um?', 'ABERTO', 1, 3, NOW()),
('API REST - Boas práticas', 'Quais são as melhores práticas para criar uma API REST? Como estruturar os endpoints?', 'ABERTO', 1, 4, NOW()),
('Git merge vs rebase', 'Qual a diferença entre git merge e git rebase? Quando usar cada um?', 'ABERTO', 1, 5, NOW());

-- Inserir algumas respostas de teste do mesmo usuário
INSERT INTO respostas (mensagem, autor_id, topico_id, data_criacao, solucao) VALUES 
('Atualização: Consegui resolver o problema com Spring Boot! Era configuração do banco mesmo.', 1, 2, NOW(), true),
('Complementando minha dúvida sobre MySQL: também gostaria de saber sobre RIGHT JOIN.', 1, 3, NOW(), false);

