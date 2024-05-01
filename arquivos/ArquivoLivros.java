package arquivos;

import aeds3.Arquivo;
import aeds3.ArvoreBMais;
import aeds3.HashExtensivel;
import aeds3.ParIntInt;
import aeds3.ListaInvertida;
import entidades.Livro;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ArquivoLivros extends Arquivo<Livro> {

  HashExtensivel<ParIsbnId> indiceIndiretoISBN;
  ArvoreBMais<ParIntInt> relLivrosDaCategoria;
  ListaInvertida lista;

  public ArquivoLivros() throws Exception {
    super("livros", Livro.class.getConstructor());
    indiceIndiretoISBN = new HashExtensivel<>(
        ParIsbnId.class.getConstructor(),
        4,
        "dados/livros_isbn.hash_d.db",
        "dados/livros_isbn.hash_c.db");
    relLivrosDaCategoria = new ArvoreBMais<>(ParIntInt.class.getConstructor(), 4, "dados/livros_categorias.btree.db");
    lista = new ListaInvertida(4, "dados/dicionario.listainv.db", "dados/blocos.listainv.db");

  }

  @Override
  public int create(Livro obj) throws Exception {
    int id = super.create(obj);
    obj.setID(id);
    String chave = obj.getTitulo();
    String[] chaves = processaStrings(chave);
    for (int i = 0; i < chaves.length; i++) {
      if (chaves[i] != null)
        lista.create(chaves[i], id);
    }

    indiceIndiretoISBN.create(new ParIsbnId(obj.getIsbn(), obj.getID()));
    relLivrosDaCategoria.create(new ParIntInt(obj.getIdCategoria(), obj.getID()));
    return id;
  }

  public Livro readISBN(String isbn) throws Exception {
    ParIsbnId pii = indiceIndiretoISBN.read(ParIsbnId.hashIsbn(isbn));
    if (pii == null)
      return null;
    int id = pii.getId();
    return super.read(id);
  }

  @Override
  public boolean delete(int id) throws Exception {
    Livro obj = super.read(id);
    if (obj != null)
      if (indiceIndiretoISBN.delete(ParIsbnId.hashIsbn(obj.getIsbn()))
          &&
          relLivrosDaCategoria.delete(new ParIntInt(obj.getIdCategoria(), obj.getID())))
        return super.delete(id);
    return false;
  }

  @Override
  public boolean update(Livro novoLivro) throws Exception {
    Livro livroAntigo = super.read(novoLivro.getID());
    if (livroAntigo != null) {

      // Testa alteração do ISBN
      if (livroAntigo.getIsbn().compareTo(novoLivro.getIsbn()) != 0) {
        indiceIndiretoISBN.delete(ParIsbnId.hashIsbn(livroAntigo.getIsbn()));
        indiceIndiretoISBN.create(new ParIsbnId(novoLivro.getIsbn(), novoLivro.getID()));
      }

      // Testa alteração da categoria
      if (livroAntigo.getIdCategoria() != novoLivro.getIdCategoria()) {
        relLivrosDaCategoria.delete(new ParIntInt(livroAntigo.getIdCategoria(), livroAntigo.getID()));
        relLivrosDaCategoria.create(new ParIntInt(novoLivro.getIdCategoria(), novoLivro.getID()));
      }

      // Atualiza o livro
      return super.update(novoLivro);
    }
    return false;
  }

  // Funcao de tratamento para os titulos

  public static String[] processaStrings(String chave) throws IOException {
    String[] chaves = chave.split(" ");
    for (int i = 0; i < chaves.length; i++) {
      chaves[i] = chaves[i].toLowerCase(); // Coloca em letras minusculas
      if (!isStopWord(chaves[i])) { // Checa se é uma stopword
        chaves[i] = removerAcentos(chaves[i]); // Se não for, termina de tratar tirando os acentos
      } else {
        chaves[i] = null; // Se for stopword, é retirada do array
      }
    }
    for (int i = 0; i < chaves.length; i++) {
      System.out.println(chaves[i]);
    }
    return chaves;
  }

  // Funcao que remove acento de palavras
  public static String removerAcentos(String texto) {
    String textoSemAcento = Normalizer.normalize(texto, Normalizer.Form.NFD);
    return textoSemAcento.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
  }

  // Função itera por arquivo contendo todas as stopwords e checa se a palavra em
  // análise é ou não uma stopword
  public static boolean isStopWord(String s) throws IOException {
    boolean resp = false;
    BufferedReader br = new BufferedReader(new FileReader("dados/stopwords.txt"));
    String line;
    while ((line = br.readLine()) != null) {
      if (s.compareTo(line.trim()) == 0) { // .trim() usado pois arquivo possui espaço em branco extra ao final de cada linha
        resp = true;
        break;
      }
    }
    br.close();
    return resp;
  }


  public void buscar(String pesquisa) throws Exception {
    //Tratamento da String pesquisada
    ArrayList<String> chaves = new ArrayList<>(Arrays.asList(processaStrings(pesquisa)));
    ArrayList<int[]> ids = new ArrayList<>();

    //Array list contendo todos os conjuntos referentes a cada palavra da busca
    for(String c : chaves) {
      ids.add(lista.read(c));
    }

    //Interseção entre os conjuntos 

    

  }

}
