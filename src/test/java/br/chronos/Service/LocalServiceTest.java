package br.chronos.Service;

import br.chronos.Entity.Local;
import br.chronos.Repository.LocalRepository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintViolationException;

import static org.mockito.Mockito.when;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class LocalServiceTest {

    @Mock
    private LocalRepository localRepository;

    @InjectMocks
    private LocalService localService;

    @DisplayName("Deve retornar um local pelo id")
    @Test
    public void testFindByIdValid() {
        Local local = new Local();
        local.setId(1L);
        local.setDescricao("Local 1");
        local.setCotaOperacao(3);
        local.setTempoOperacao(60);

        when(localRepository.findById(1L)).thenReturn(Optional.of(local));

        Local returnedLocal = localService.findById(local.getId());

        assertInstanceOf(Local.class, returnedLocal);
        assertEquals(local.getId(), returnedLocal.getId());
    }

    @DisplayName("Deve retornar null quando o local não for encontrado")
    @Test
    public void testFindByIdInvalid() {
        when(localRepository.findById(1L)).thenReturn(Optional.empty());

        Local returnedLocal = localService.findById(1L);

        assertNull(returnedLocal);
    }

    @DisplayName("Deve salvar um local")
    @Test
    public void testSaveValid() throws Exception {
        Local local = new Local();
        local.setId(1L);
        local.setDescricao("Local 1");
        local.setCotaOperacao(1);
        local.setTempoOperacao(30);

        when(localRepository.save(local)).thenReturn(local);

        Local returnedLocal = localService.save(local);

        assertInstanceOf(Local.class, returnedLocal);
        assertEquals(local.getId(), returnedLocal.getId());
    }

    @DisplayName("Deve lançar uma exceção ao tentar salvar um local inválido")
    @Test
    public void testThrowExceptionSaveLocal() throws Exception {
        Local local = new Local();

        when(localRepository.save(local)).thenThrow(ConstraintViolationException.class);

        assertThrows(ConstraintViolationException.class, () -> {
            localService.save(local);
        });
    }
}
