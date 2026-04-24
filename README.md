
  <h2> BoleteiroOn - Sistema de Gestão de Leilões Presencial</h2>

  O BoleteiroOn é uma solução robusta para gestão de leilões, focada na automação do processo de arrematação, geração de documentos digitais e segurança. O sistema permite o controle completo desde o cadastro de leilões e lotes
  até a assinatura digital e emissão do Auto de Arrematação em PDF.

  <h3> Funcionalidades Principais</h3>

   - Gestão de Leilões: CRUD completo de leilões com suporte a imagens e metadados específicos (comitente, leiloeiro, editais).
   - Controle de Lotes: Organização de itens por leilão.
   - Gestão de Arrematantes: Cadastro detalhado de participantes presenciais e online.
   - Processo de Arrematação:
       - Registro de vendas presenciais e online.
       - Cálculo automático de comissão (5%).
       - Assinatura Digital: Captura de fotos e assinaturas em Base64.
       - Automação de Documentos: Geração automática de Auto de Arrematação em PDF.
   - Integrações:
       - AWS S3: Armazenamento seguro de fotos de assinaturas e documentos PDF.
       - Resend/Spring Mail: Envio automatizado de e-mails com os documentos gerados.
       - ViaCEP: Integração para busca automática de endereços.
   - Segurança:
       - Autenticação via JWT (JSON Web Token).
       - Controle de acesso baseado em perfis (Admin, Boleteiro).
   - Linguagem: Java 17
   - Framework: Spring Boot 4.0.3
   - Persistência: Spring Data JPA / Hibernate
   - Banco de Dados: PostgreSQL 15
   - Migrações: Flyway
   - Segurança: Spring Security + JWT (Auth0)
   - Documentação PDF: OpenHTMLtoPDF
   - Storage: AWS SDK (S3)
   - Comunicação: Spring Cloud OpenFeign
   - Utilitários: Lombok, Jakarta Validation
