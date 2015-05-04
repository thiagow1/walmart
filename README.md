# Walmart
##Desafio Entregando Mercadorias
**Descrição**

O Walmart esta desenvolvendo um novo sistema de logistica e sua
ajuda é muito importante neste momento. Sua tarefa será desenvolver
o novo sistema de entregas visando sempre o menor custo. Para
popular sua base de dados o sistema precisa expor um Webservices que
aceite o formato de malha logística (exemplo abaixo), nesta mesma
requisição o requisitante deverá informar um nome para este mapa. É
importante que os mapas sejam persistidos para evitar que a cada
novo deploy todas as informações desapareçam. O formato de malha
logística é bastante simples, cada linha mostra uma rota: ponto de
origem, ponto de destino e distância entre os pontos em quilômetros.

<pre>
A B 10
B D 15
A C 20
C D 30
B E 50
D E 30
</pre>

Com os mapas carregados o requisitante irá procurar o menor valor de
entrega e seu caminho, para isso ele passará o nome do ponto de
origem, nome do ponto de destino, autonomia do caminhão (km/l) e o
valor do litro do combustivel, agora sua tarefa é criar este
Webservices. Um exemplo de entrada seria, origem A, destino D,
autonomia 10, valor do litro 2,50; a resposta seria a rota A B D com
custo de 6,25.

Voce está livre para definir a melhor arquitetura e tecnologias para
solucionar este desafio, mas não se esqueça de contar sua motivação
no arquivo README que deve acompanhar sua solução, junto com os
detalhes de como executar seu programa. Documentação e testes serão
avaliados também =) Lembre-se de que iremos executar seu código com
malhas beeemm mais complexas, por isso é importante pensar em
requisitos não funcionais também!!

Também gostariamos de acompanhar o desenvolvimento da sua aplicação
através do código fonte. Por isso, solicitamos a criação de um
repositório que seja compartilhado com a gente. Para o
desenvolvimento desse sistema, nós solicitamos que você utilize a
sua (ou as suas) linguagem de programação principal. Pode ser Java,
JavaScript ou Ruby.

**Ambiente Utilizado**

* Windows 8.1 64 bits
* Eclipse Luna 
 * https://www.eclipse.org/downloads/download.php?file=/technology/epp/downloads/release/luna/SR2/eclipse-jee-luna-SR2-win32-x86_64.zip
* Linguagem Java 8
 * http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html
* Servidor WildFly 8.2.0.Final
 * http://download.jboss.org/wildfly/8.2.0.Final/wildfly-8.2.0.Final.zip
* Apache Maven 3.3.3
 * https://maven.apache.org/download.cgi
* Banco de Dados H2 Embedded (Não precisa de instalação)

**Frameworks Utilizados**

* Maven
* JEE 7
* JPA
* Hibernate 4.3.6
* ReastEasy do Jboss 
* Jersey-Client para o client

**Configuração do H2**

É necessário incluir o código abaixo na área de datasources no standalone.xml do WildFly

<pre>
<code>
&lt;datasource jndi-name=&quot;java:/walmart&quot; pool-name=&quot;walmart&quot; enabled=&quot;true&quot; use-java-context=&quot;true&quot;&gt; 
&lt;connection-url&gt;jdbc:h2:~/walmart&lt;/connection-url&gt; 
&lt;driver&gt;h2&lt;/driver&gt; 
&lt;security&gt; 
&lt;user-name&gt;sa&lt;/user-name&gt; 
&lt;/security&gt; 
&lt;/datasource&gt;
</code>
</pre>

## Observações
* O Banco de dados é criado automaticamente na pasta do Usuário ao iniciar o servidor, caso não exista.
* Foram implementados testes JUnit no projeto walmart-logistica-service.
* No projeto walmart-logistica-ws-client é possível executar chamadas ao webService disponibilizado utilizando o Jersey-Client.