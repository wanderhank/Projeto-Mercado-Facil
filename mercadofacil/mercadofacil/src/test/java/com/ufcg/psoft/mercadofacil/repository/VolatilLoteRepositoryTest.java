package com.ufcg.psoft.mercadofacil.repositories;

import com.ufcg.psoft.mercadofacil.models.Lote;
import com.ufcg.psoft.mercadofacil.models.Produto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class VolatilLoteRepositoryTest {

    @Autowired
    LoteRepository<Lote, Long> driver;

    Lote lote;
    Lote resultado;
    Produto produto;

    @BeforeEach
    void setup() {
        produto = Produto.builder()
                .id(1L)
                .nome("Produto Base")
                .codigoBarra("123456789")
                .fabricante("Fabricante Base")
                .preco(125.36)
                .build();
        lote = Lote.builder()
                .id(1L)
                .numeroDeItens(100)
                .produto(produto)
                .build();
    }
    @AfterEach
    void tearDown() {
        produto = null;
        driver.deleteAll();
    }

    @Test
    @DisplayName("Adicionar o primeiro Lote no repositorio de dados")
    void salvarPrimeiroLote() {
        resultado = driver.save(lote);

        Assertions.assertEquals(driver.findAll().size(),1);
        Assertions.assertEquals(resultado.getId().longValue(), lote.getId().longValue());
        Assertions.assertEquals(resultado.getProduto(), produto);
    }
    @Test
    @DisplayName("Adicionar o segundo Lote (ou posterior) no repositorio de dados")
    void salvarSegundoLoteOuPosterior() {
        Produto produtoExtra = Produto.builder()
                .id(2L)
                .nome("Produto Extra")
                .codigoBarra("987654321")
                .fabricante("Fabricante Extra")
                .preco(125.36)
                .build();
        Lote loteExtra = Lote.builder()
                .id(2L)
                .numeroDeItens(100)
                .produto(produtoExtra)
                .build();
        driver.save(lote);

        resultado = driver.save(loteExtra);

        Assertions.assertEquals(driver.findAll().size(),2);
        Assertions.assertEquals(resultado.getId().longValue(), loteExtra.getId().longValue());
        Assertions.assertEquals(resultado.getProduto(), produtoExtra);

    }
    @Test
    @DisplayName("recuperar um lote atraves de um id")
    void recuperandoLote() {
        Lote loteget = criaLote(3L);

        driver.save(loteget);

        Assertions.assertEquals(driver.find(3L),loteget);
        Assertions.assertNull(driver.find(100L));
    }
    @Test
    @DisplayName("deve retornar todos os lotes")
    void buscandoTodosOsLotes() {
        Lote lotefindall = criaLote(3L);

        driver.save(lotefindall);
        Assertions.assertEquals(driver.findAll().size(),1);

        driver.save(lote);
        Assertions.assertEquals(driver.findAll().size(),2);

    }

    @Test
    @DisplayName("Atualizando um lote especifico")
    void deveAtualizarUmLote() {
        driver.save(criaLote(3L));
        driver.save(lote);
        Assertions.assertEquals(driver.findAll().size(), 2);

        Assertions.assertEquals(lote.getProduto().getNome(), "Produto Base");

        lote.getProduto().setNome("máquina de café expresso");
        driver.update(lote);

        Lote loteUpdate = driver.find(lote.getId());
        Assertions.assertEquals(loteUpdate.getProduto().getNome(), "máquina de café expresso");

        Assertions.assertEquals(driver.findAll().size(), 2);

        Lote loteNaoSalvo = criaLote(8L);
        Assertions.assertNull(driver.update(loteNaoSalvo));
    }

    @Test
    @DisplayName("apagando apenas um lote")
    void deveApagarUmLote() {
        Lote lote1 = criaLote(1L);
        driver.save(lote1);

        Lote lote2 = criaLote(2L);
        driver.save(lote2);

        Assertions.assertEquals(driver.findAll().size(), 2);
        driver.delete(lote1);
        Assertions.assertEquals(driver.findAll().size(), 1);
    }

    @Test
    @DisplayName("Apagando todos os lotes")
    void deveApagarTodosOsLotes() {
        Lote lote1 = criaLote(1L);
        driver.save(lote1);

        Lote lote2 = criaLote(2L);
        driver.save(lote2);

        Assertions.assertEquals(driver.findAll().size(), 2);
        driver.deleteAll();
        Assertions.assertEquals(driver.findAll().size(), 0);
    }

    private Lote criaLote(Long idLote) {
        Produto produto = Produto.builder()
                .id(3L)
                .nome("cafeteira")
                .codigoBarra("91662593")
                .fabricante("extra rap")
                .preco(60)
                .build();
        Lote loteCreate = Lote.builder()
                .id(idLote)
                .numeroDeItens(5)
                .produto(produto)
                .build();

        return loteCreate;
    }
}

