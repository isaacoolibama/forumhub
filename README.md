# 🚀 FórumHub - Sistema de Fórum com Spring Boot

<div align="center">

## 🎯 **Sobre o Projeto**

**FórumHub** - Sistema completo de fórum com Spring Boot para formação **Java e Spring Framework G8 - ONE** da Oracle em parceria com a Alura LATAM

</div>

Este projeto demonstra o domínio completo das tecnologias Java modernas, incluindo:
- Spring Boot para desenvolvimento web
- Spring Security para autenticação e autorização
- JPA/Hibernate para persistência de dados
- APIs RESTful com validações
- Sistema de perfis e controle de acesso
- Autenticação JWT stateless
- Tratamento de erros e respostas padronizadas

---

<div align="center">

### 🛠️ **Stack Tecnológica**

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=spring-security&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-00000F?style=for-the-badge&logo=mysql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2CA5E0?style=for-the-badge&logo=docker&logoColor=white)
![Maven](https://img.shields.io/badge/apache_maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)

</div>

---

<div align="center">

### 📊 **Estatísticas do Projeto**

![GitHub last commit](https://img.shields.io/github/last-commit/isaacoolibama/forumhub)
![GitHub repo size](https://img.shields.io/github/repo-size/isaacoolibama/forumhub)
![GitHub language count](https://img.shields.io/github/languages/count/isaacoolibama/forumhub)

</div>

---

### 🚀 **Funcionalidades Principais**

**🔐 Sistema de Autenticação**
- Cadastro de usuários com perfis específicos (ADMIN ou USUARIO)
- Login JWT com controle de acesso baseado em perfis
- Spring Security com filtros personalizados

**📝 Gestão de Tópicos**
- Criação apenas para usuários autenticados
- Edição/deleção apenas para autor ou administradores
- Visualização pública para todos
- Relacionamentos com usuários e cursos

**👥 Gestão de Usuários**
- Perfis diferenciados (ADMIN/USUARIO)
- Cadastro flexível com escolha de perfil
- Validações e criptografia BCrypt

**🌍 Gestão de Cursos**
- Categorização de tópicos por cursos
- Sistema flexível de relacionamentos

---

### 🏗️ **Arquitetura do Sistema**

**📁 Estrutura de Pacotes**
```
src/main/java/br/com/alura/forum/hub/
├── config/           # Configurações do Spring Security
├── controller/       # Controladores REST
├── dto/             # Data Transfer Objects
├── model/           # Entidades JPA
├── repository/      # Interfaces de acesso a dados
├── service/         # Lógica de negócio e segurança
└── HubApplication.java
```

**🔄 Fluxo de Dados**
1. Cliente → Controller → Service → Repository → Database
2. Autenticação → JWT Filter → Spring Security → Authorization
3. DTOs ↔ Entities via Controllers

**🗄️ Modelo de Dados**
- **Usuario**: Usuários com perfis e relacionamentos
- **Topico**: Tópicos do fórum com autor e curso
- **Curso**: Categorias de tópicos
- **Perfil**: Definição de roles (ADMIN/USUARIO)

---

### 🐳 **Configuração com Docker**

O projeto inclui configuração Docker completa para facilitar o desenvolvimento:

```bash
# Subir o banco MySQL
docker-compose up -d

# Verificar status
docker ps

# Parar serviços
docker-compose down
```

**📊 Banco de Dados**
- **MySQL 8.0** rodando na porta 3307
- **Database**: forumhub
- **Usuário**: forumhub
- **Senha**: forumhub123
- **Dados iniciais** incluídos no `V1__init.sql`

---

### 🚀 **Como Executar**

**1. Clone e Setup**
```bash
git clone <seu-repositorio>
cd hub
```

**2. Configure o Banco**
```bash
# Subir MySQL com Docker
docker-compose up -d

# As tabelas são criadas automaticamente pelo Flyway
# Dados iniciais já estão incluídos
```

**3. Execute a Aplicação**
```bash
# Compile
mvn compile

# Execute
mvn spring-boot:run
```

**🌐 Aplicação disponível em**: `http://localhost:8080`

---

### 🔐 **Endpoints da API**

**🔑 Autenticação**
- **POST** `/auth/cadastro` - Cadastro de usuários
- **POST** `/auth/login` - Login e obtenção de token JWT

**📝 Tópicos**
- **GET** `/topicos` - Listar todos os tópicos (público)
- **GET** `/topicos/{id}` - Buscar tópico por ID (público)
- **POST** `/topicos` - Criar tópico (autenticado)
- **PUT** `/topicos/{id}` - Atualizar tópico (autor ou admin)
- **DELETE** `/topicos/{id}` - Deletar tópico (autor ou admin)

---

### 📊 **Regras de Negócio**

**🔒 Controle de Acesso**
- **Endpoints públicos**: Visualização de tópicos
- **Endpoints protegidos**: Criação, edição e deleção de tópicos
- **Autenticação obrigatória**: Para operações de modificação
- **Autorização baseada em perfil**: ADMIN pode tudo, USUARIO apenas seus tópicos

**👤 Perfis de Usuário**
- **ADMIN (tipoPerfil: 2)**: Acesso total ao sistema
- **USUARIO (tipoPerfil: 1)**: Acesso limitado aos próprios tópicos

**📝 Gestão de Tópicos**
- **Criação**: Apenas usuários autenticados
- **Edição**: Apenas o autor ou administradores
- **Deleção**: Apenas o autor ou administradores
- **Visualização**: Pública para todos

---

### 🧪 **Testando a API**

**📱 Postman Collection**
O projeto inclui uma coleção completa do Postman (`ForumHub_API.postman_collection.json`) com:
- Testes de autenticação
- Testes de criação de tópicos
- Testes de segurança
- Exemplos de dados válidos

**🔍 Fluxo de Teste Recomendado**
1. **Cadastre um usuário admin** → `POST /auth/cadastro`
2. **Faça login** → `POST /auth/login`
3. **Use o token** para criar tópicos → `POST /topicos`
4. **Teste as regras de segurança** com usuários diferentes

---

### 🚨 **Tratamento de Erros**

**🔐 Erros de Autenticação**
- **401 Unauthorized**: Token inválido ou ausente
- **403 Forbidden**: Acesso negado (não é autor ou admin)

**📝 Erros de Validação**
- **400 Bad Request**: Dados inválidos na requisição
- **404 Not Found**: Recurso não encontrado
- **500 Internal Server Error**: Erro interno do servidor

---

### 🔧 **Configurações Avançadas**

**⚡ Performance**
```properties
# Pool de conexões
spring.datasource.hikari.maximum-pool-size=10

# Cache JPA
spring.jpa.properties.hibernate.cache.use_second_level_cache=false
```

**🔐 Segurança JWT**
```properties
# Chave secreta para JWT
jwt.secret=sua-chave-secreta-aqui
jwt.expiration=86400000
```

---

<div align="center">

## 👨‍💻 **Desenvolvedor**

### 🚀 **Isaac Oolibama Ramos Lacerda**

[![GitHub](https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white)](https://github.com/isaacoolibama)
[![LinkedIn](https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/isaaclacerda/)

</div>

---

### 📚 **Recursos de Aprendizagem**

* [Spring Boot Documentation](https://spring.io/projects/spring-boot)
* [Spring Security Reference](https://docs.spring.io/spring-security/reference/)
* [JPA Specification](https://jakarta.ee/specifications/persistence/)
* [JWT Introduction](https://jwt.io/introduction)

---

### 📄 **Licença**

Este projeto está licenciado sob a Licença MIT - veja o arquivo [LICENSE](LICENSE) para detalhes.

```
MIT License

Copyright (c) 2025 Isaac Oolibama Ramos Lacerda

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

---

Este projeto foi desenvolvido como parte da formação Oracle ONE G8 - Alura LATAM.
