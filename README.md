# ReCicle

O **ReCicle** é uma aplicação desktop desenvolvida em Java com o objetivo de gerenciar o registro e o anúncio de aparelhos celulares. 

## 🚀 Funcionalidades

* **Home:** Tela inicial da aplicação para navegação.
* **Cadastro de Celulares (`RegisterPhone`):** Interface dedicada para inserir e registrar os dados de novos aparelhos no sistema.
* **Celulares Anunciados (`AnnouncedPhones`):** Tela para visualização e listagem dos aparelhos que já estão cadastrados e anunciados.
* **Banco de Dados:** Persistência de dados integrada para garantir que todos os registros sejam salvos de forma segura.

## 🛠️ Tecnologias Utilizadas

* **Java:** Linguagem de programação principal.
* **Interface Gráfica:** Telas construídas com Java (pacote `view`).
* **Banco de Dados:** Gerenciamento de conexão e persistência de dados.

## 📁 Estrutura do Projeto

O código-fonte está organizado na seguinte estrutura de pacotes:

* `com.mycompany.recicle`
  * `ReCicle.java` (Classe Principal que inicia a aplicação)
  * `database/`
    * `Database.java` (Classe responsável pela conexão e operações no banco)
  * `view/`
    * `Home.java` (Tela Principal)
    * `RegisterPhone.java` (Tela de Cadastro)
    * `AnnouncedPhones.java` (Tela de Listagem de Anúncios)

## 🖼️ Recursos Visuais

O projeto utiliza assets próprios localizados no diretório de recursos (`resources/images/`), incluindo:
* `icon.png` (Ícone da aplicação)
* `logotipo.png` (Logo oficial do ReCicle)
* `celular.png` (Imagens de apoio das telas)

## ⚙️ Como Executar o Projeto

1. Faça o clone deste repositório:
   ```bash
   git clone [https://github.com/](https://github.com/)[SEU_USUARIO]/ReCicle.git
   
