package es.wasabiweb.junit.app.models;

import es.wasabiweb.junit.app.models.exceptions.DineroInsuficienteException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CuentaTest {

    @Test
    @DisplayName("Probando el nombre de la cuenta")
    void testNombreCuenta(){

        Cuenta cuenta = new Cuenta("Andres", new BigDecimal("100.0000123"));
        //cuenta.setPersona("Alvaro");
        String esperado = "Andres";
        String real = cuenta.getPersona();
        assertNotNull(real);
        assertEquals(esperado, real, "El nombre tenia que ser " + esperado);
        assertEquals("Andres", real, () -> "El nombre tenia que ser " + esperado);

    }

    @Test
    @Disabled
    void testSaldoCuenta(){

        Cuenta cuenta = new Cuenta("Alvaro", new BigDecimal("1000.0000123"));
        assertNotNull(cuenta.getSaldo());

        assertEquals(1000.0000123, cuenta.getSaldo().doubleValue());
        assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
    }

    @Test
    void testReferenciaDeCuenta() {

        Cuenta cuenta = new Cuenta("Pepe Lopez", new BigDecimal("8900.233"));
        Cuenta cuenta2 = new Cuenta("Pepe Lopez", new BigDecimal("8900.233"));

        //assertNotEquals(cuenta, cuenta2);
        assertEquals(cuenta, cuenta2);
    }

    @Test
    void testDebito(){
        Cuenta cuenta = new Cuenta("Pepe Lopez", new BigDecimal("8900.233"));
        cuenta.debito(new BigDecimal(100));
        assertNotNull(cuenta.getSaldo());
        assertEquals(8800, cuenta.getSaldo().intValue());
        assertEquals("8800.233", cuenta.getSaldo().toPlainString());
    }

    @Test
    void testCredito(){
        Cuenta cuenta = new Cuenta("Pepe Lopez", new BigDecimal("8900.233"));
        cuenta.credito(new BigDecimal(100));
        assertNotNull(cuenta.getSaldo());
        assertEquals(9000, cuenta.getSaldo().intValue());
        assertEquals("9000.233", cuenta.getSaldo().toPlainString());
    }

    @Test
    void testDineroInsuficienteException(){
        Cuenta cuenta = new Cuenta("Pepe Lopez", new BigDecimal("1000.12345"));
        Exception exception = assertThrows(DineroInsuficienteException.class, () -> {
            cuenta.debito(new BigDecimal(10000));
        });

        String actual = exception.getMessage();
        String esperado = "Dinero insuficiente";
        assertEquals(esperado, actual);
    }
}