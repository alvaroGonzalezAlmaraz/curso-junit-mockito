package es.wasabiweb.junit.app.models;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class BancoTest {

    @Test
    void transferirTest(){
        Cuenta cuenta = new Cuenta("Pepe Lopez", new BigDecimal("1000"));
        Cuenta cuenta2 = new Cuenta("Alvaro Lopez", new BigDecimal("1000"));

        Banco banco = new Banco();
        banco.setNombre("ing");
        banco.transferir(cuenta, cuenta2, new BigDecimal(500));

        assertEquals("1500",cuenta2.getSaldo().toPlainString());
        assertEquals("500", cuenta.getSaldo().toPlainString());
    }

    @Test
    void testRealacionBancoCuenta(){
        Cuenta cuenta = new Cuenta("Pepe Lopez", new BigDecimal("1000"));
        Cuenta cuenta2 = new Cuenta("Alvaro Lopez", new BigDecimal("1000"));

        Banco banco = new Banco();

        banco.addCuenta(cuenta);
        banco.addCuenta(cuenta2);

        banco.setNombre("ing");
        banco.transferir(cuenta, cuenta2, new BigDecimal(500));

        assertAll(()-> assertEquals("1500",cuenta2.getSaldo().toPlainString()),
                ()-> assertEquals("500", cuenta.getSaldo().toPlainString()),
                ()-> assertEquals(2, banco.getCuentas().size()),
                ()-> assertEquals("ing", cuenta.getBanco().getNombre()),
                ()-> assertEquals("Alvaro Lopez", banco.getCuentas().stream()
                        .filter(c -> c.getPersona().equals("Alvaro Lopez"))
                        .findFirst()
                        .get().getPersona()),
                ()-> assertTrue(banco.getCuentas().stream()
                    .anyMatch(c -> c.getPersona().equals("Alvaro Lopez"))));
    }
}
