package br.chronos.Service;

import br.chronos.Entity.Local;
import br.chronos.Repository.LocalRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class LocalServiceIntegrationTest {

    @Autowired
    private LocalService localService;

    @Autowired
    private LocalRepository localRepository;

    @Test
    public void testSaveLocal() throws Exception {
        Local local = new Local();

        local.setDescricao("Teste Local");
        local.setPermitirChamadaUsina(true);
        local.setPermitirCarregamento(false);
        local.setTempoOperacao(120);
        local.setCotaOperacao(10);

        Local savedLocal = localService.save(local);

        assertNotNull(savedLocal.getId());
        assertEquals(local.getDescricao(), savedLocal.getDescricao());

        Local foundLocal = localRepository.findById(savedLocal.getId()).orElse(null);
        assertNotNull(foundLocal);
        assertEquals(local.getDescricao(), foundLocal.getDescricao());
    }

    @Test
    void findByIdShouldReturnLocal() throws Exception {
        Local local = new Local();
        local.setDescricao("Teste Local");

        localService.save(local);

        Local foundLocal = localService.findById(local.getId());
        assertNotNull(foundLocal);
    }

    @Test
    void findByIdShouldReturnNullWhenLocalNotFound() {
        Local foundLocal = localService.findById(999L);

        assertNull(foundLocal);
    }

    @Test
    void findAllShouldReturnPageOfLocals() throws Exception {
        Local local = new Local();
        local.setDescricao("Teste Local 1");

        Local local2 = new Local();
        local.setDescricao("Teste local 2");

        localService.save(local);
        localService.save(local2);

//        assertEquals(1, localService.findAll().getTotalElements());
    }
}