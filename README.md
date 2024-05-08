# TP-2-AEDS3

Autores: Gabriel Samarane, João Madeira

O projeto insere uma estrutura de Lista Invertida em uma aplicação CRUD que controla um banco de dados sobre Livros (Utilizada no primeiro TP).
A proposta principal é estabelecer uma Lista Invertida contendo partes dos títulos de cada livro como chave e os ids de cada livro que possuem 
essa palavra no título como os dados. Dessa forma, as funções do CRUD (inserção, alteração, remoção e leitura) foram atualizadas em prol de sustentarem 
essa estrutura de lista. De tal forma, todo livro adicionado, alterado ou removido tem seu título e seu id computados de forma correta na lista.

Um exemplo, é no momento de inserção de qualquer livro, em que seu título é separado em cada palavra que o compõem (removendo stop words que estão
descritas em um arquivo texto), depois as informações são tratadas, removendo os acentos e deixando as letras minúsculas, para finalmente, serem 
adicionadas na lista invertida junto do ID do livro inserido.

Dessa forma, também foi desenvolvida uma função que busca os livros com base em uma pesquisa de título, devolvendo o conjunto de livros que possuem
as palavras enviadas na busca.


CheckList:

1 - A inclusão de um livro acrescenta os termos do seu título à lista invertida?
  - Sim
2 - A alteração de um livro modifica a lista invertida removendo ou acrescentando termos do título?
  - Sim
3 - A remoção de um livro gera a remoção dos termos do seu título na lista invertida? 
  - Sim
4 - Há uma busca por palavras que retorna os livros que possuam essas palavras?
  - Sim
5 - Essa busca pode ser feita com mais de uma palavra?
  - Sim
6 - As stop words foram removidas de todo o processo?
  - Sim
7 - Que modificação, se alguma, você fez para além dos requisitos mínimos desta tarefa?
  - Nenhuma
8 - O trabalho está funcionando corretamente?
  - Sim
9 - O trabalho está completo?
  - Sim
10 - O trabalho é original e não a cópia de um trabalho de um colega?
  - Sim
