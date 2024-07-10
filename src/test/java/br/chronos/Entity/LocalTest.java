package br.chronos.Entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import javax.validation.Validator;

import static org.junit.jupiter.api.Assertions.*;

class LocalTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @DisplayName("Lança exceção ao validar descrição nula")
    @Test
    public void shouldThrowExceptionOnNullDescricao() {
        Local local = new Local();
        local.setCotaOperacao(2);
        local.setTempoOperacao(60);
        local.setDescricao(null);

        Set<ConstraintViolation<Local>> violations = validator.validate(local);

        assertEquals(1, violations.size());
        assertEquals("Descrição não pode ser nula", violations.iterator().next().getMessage());
    }

    @DisplayName("Lança exceção ao validar tempo de operação nulo")
    @Test
    public void shouldThrowExceptionOnNullTempoOperacao() {
        Local local = new Local();
        local.setCotaOperacao(2);
        local.setTempoOperacao(null);
        local.setDescricao("descrição");

        Set<ConstraintViolation<Local>> violations = validator.validate(local);

        assertEquals(1, violations.size());
        assertEquals("Tempo de operação não pode ser nulo", violations.iterator().next().getMessage());
    }

    @DisplayName("Lança exceção ao validar cota de operação nula")
    @Test
    public void shouldThrowExceptionOnNullCotaOperacao() {
        Local local = new Local();
        local.setCotaOperacao(null);
        local.setTempoOperacao(60);
        local.setDescricao("descrição");

        Set<ConstraintViolation<Local>> violations = validator.validate(local);

        assertEquals(1, violations.size());
        assertEquals("Cota de operação não pode ser nula", violations.iterator().next().getMessage());
    }

    @DisplayName("Todas os campos são válidos")
    @Test
    public void shouldNotThrowExceptionOnValidFields() {
        Local local = new Local();
        local.setCotaOperacao(2);
        local.setTempoOperacao(60);
        local.setDescricao("descrição");

        Set<ConstraintViolation<Local>> violations = validator.validate(local);

        assertTrue(violations.isEmpty());
    }
}