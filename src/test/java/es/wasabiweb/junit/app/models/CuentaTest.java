package es.wasabiweb.junit.app.models;

import es.wasabiweb.junit.app.models.exceptions.DineroInsuficienteException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CuentaTest {

    Cuenta cuenta;

    @BeforeEach
    void initMetodoTest() {
        this.cuenta = new Cuenta("Andres", new BigDecimal("1000"));
        System.out.println("iniciando el método");

    }

    @AfterAll
    static void afterAll() {
        System.out.println("Finalizando método");
    }

    @Test
    @DisplayName("Probando el nombre de la cuenta")
    void testNombreCuenta() {
        //cuenta.setPersona("Alvaro");
        String esperado = "Andres";
        String real = cuenta.getPersona();
        assertNotNull(real);
        assertEquals(esperado, real, "El nombre tenia que ser " + esperado);
        assertEquals("Andres", real, () -> "El nombre tenia que ser " + esperado);

    }


    @Test
    void testReferenciaDeCuenta() {

        Cuenta cuenta2 = new Cuenta("Pepe Lopez", new BigDecimal("8900.233"));

        assertNotEquals(cuenta, cuenta2);
        //assertEquals(cuenta, cuenta2);
    }

    @Test
    void testDebito() {

        cuenta.debito(new BigDecimal(100.000000));
        assertNotNull(cuenta.getSaldo());
        assertEquals(900, cuenta.getSaldo().intValue());
        assertEquals("900.0000123", cuenta.getSaldo().toPlainString());
    }

    @Test
    void testCredito() {

        cuenta.credito(new BigDecimal(100));
        assertNotNull(cuenta.getSaldo());
        assertEquals(1100, cuenta.getSaldo().intValue());
        assertEquals("1100.0000123", cuenta.getSaldo().toPlainString());
    }

    @Test
    void testDineroInsuficienteException() {

        Exception exception = assertThrows(DineroInsuficienteException.class, () -> {
            cuenta.debito(new BigDecimal(10000));
        });

        String actual = exception.getMessage();
        String esperado = "Dinero insuficiente";
        assertEquals(esperado, actual);
    }

    @Nested
    @DisplayName("Test sobre el sistema operativo")
    class sistemaOperativo {

        @Test
        @EnabledOnOs(OS.WINDOWS)
        void testSoloWindows() {
        }

        @Test
        @EnabledOnOs({OS.LINUX, OS.SOLARIS})
        void testSoloLinux() {
        }

        @Test
        @EnabledOnJre(JRE.JAVA_8)
        void soloJava() {
        }

    }


    @Nested
    class variablesPropiedades {

        @Test
        void imprimirSystemProperties() {
            Properties propiedades = System.getProperties();
            propiedades.forEach((k, v) -> System.out.println(k + ": " + v));
        }

        @Test
        @EnabledIfSystemProperty(named = "java.version", matches = "15.*.*")
        void javaVersion() {
        }

        @Test
        @EnabledIfSystemProperty(named = "os.arch", matches = "amd64")
        void archVersion() {
        }

        @Test
        @EnabledIfSystemProperty(named = "user.name", matches = "ALVARO")
        void user() {
        }

        @Test
        @EnabledIfSystemProperty(named = "ENV", matches = "dev")
        void testDev() {
        }

    }

    @Nested
    class variablesEntorno {

        @Test
        void imprimirVariablesAmbiente() {
            Map<String, String> env = System.getenv();
            env.forEach((k, v) -> System.out.println(k + " " + v));
        }

        @Test
        @EnabledIfEnvironmentVariable(named = "SESSIONNAME", matches = "Console")
        void variablesSistema() {
        }

        @Test
        @EnabledIfEnvironmentVariable(named = "ENVIROMENT", matches = "desarrollo")
        void testEnv() {
        }

    }

    @Test
    //@Disabled
    void testSaldoCuentaDev() {

        boolean esDev = "desarrollo".equals(System.getenv("ENVIROMENT"));
        assumeTrue(esDev);
        assertNotNull(cuenta.getSaldo());
        assertEquals(1000.0000123, cuenta.getSaldo().doubleValue());
        assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
    }

    @Test
    //@Disabled
    void testSaldoCuentaDev2() {

        boolean esDev = "desarrollo".equals(System.getenv("ENVIROMENT"));
        assumingThat(esDev, () -> {
            assertNotNull(cuenta.getSaldo());
            assertEquals(1000.0000123, cuenta.getSaldo().doubleValue());
            assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);

        });

    }

    @DisplayName("Probando las repeticiones:")
    @RepeatedTest(value = 5, name = "{displayName} repeticion numero {currentRepetition} de {totalRepetitions}")
    //@Disabled
    void testSaldoCuenta(RepetitionInfo info) {

        if (info.getCurrentRepetition() == 3) {
            System.out.println("Hola, estamos en la repeticion: " + info.getCurrentRepetition());
        }
        assertNotNull(cuenta.getSaldo());

        assertEquals(1000.0000123, cuenta.getSaldo().doubleValue());
        assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
    }

    @Nested
    class pruebasParametrizadas{

        @ParameterizedTest(name = "Test numero {index} ejecutando con el valor {0} - {argumentsWithNames}")
        @ValueSource(strings = {"100", "200", "300", "1000"})
        void testDebitoParametrizado(String cantidad) {
            cuenta.debito(new BigDecimal(cantidad));
            assertNotNull(cuenta.getSaldo());
            System.out.println(cuenta.getSaldo());
            assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) >= 0);

        }

        @ParameterizedTest(name = "Test numero {index} ejecutando con el valor {0} - {argumentsWithNames}")
        @CsvSource({"1,100", "2,200", "3,300", "4,1000"})
        void testDebitoParametrizadCsvSource(String index, String cantidad) {
            System.out.println(index + "->" + cantidad);
            cuenta.debito(new BigDecimal(cantidad));
            assertNotNull(cuenta.getSaldo());
            assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) >= 0);

        }

        @ParameterizedTest(name = "Test numero {index} ejecutando con el valor {0} - {argumentsWithNames}")
        @CsvFileSource(resources = "/data.csv")
        void testDebitoParametrizadCsvFileSource(String cantidad) {
            //System.out.println(index + "->" + cantidad);
            cuenta.debito(new BigDecimal(cantidad));
            assertNotNull(cuenta.getSaldo());
            assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) >= 0);

        }



        @ParameterizedTest(name = "Test numero {index} ejecutando con el valor {0} - {argumentsWithNames}")
        @CsvSource({"200,100, John, Andres", "250,200,Juan,Juan", "310,300, Pedro, Pedrito", "1000,1000,Steve, Steve"})
        void testDebitoParametrizadCsvSource2(String saldo, String cantidad, String esperado, String actual) {
            System.out.println(saldo + "->" + cantidad);
            cuenta.setSaldo(new BigDecimal(saldo));
            cuenta.debito(new BigDecimal(cantidad));
            cuenta.setPersona(actual);
            assertNotNull(cuenta.getSaldo());
            assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);

            assertNotNull(cuenta.getPersona());
            assertEquals(esperado,actual);

        }

        @ParameterizedTest(name = "Test numero {index} ejecutando con el valor {0} - {argumentsWithNames}")
        @CsvFileSource(resources = "/data2.csv")
        void testDebitoParametrizadCsvSourceFile2(String saldo, String cantidad, String esperado, String actual) {
            System.out.println(saldo + "->" + cantidad);
            cuenta.setSaldo(new BigDecimal(saldo));
            cuenta.debito(new BigDecimal(cantidad));
            cuenta.setPersona(actual);
            assertNotNull(cuenta.getSaldo());
            assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);

            assertNotNull(cuenta.getPersona());
            assertEquals(esperado,actual);

        }

    }

    @ParameterizedTest(name = "Test numero {index} ejecutando con el valor {0} - {argumentsWithNames}")
    @MethodSource("cantidadList")
    void testDebitoParametrizadMethodSource(String cantidad) {
        //System.out.println(index + "->" + cantidad);
        cuenta.debito(new BigDecimal(cantidad));
        assertNotNull(cuenta.getSaldo());
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) >= 0);

    }

    static List<String> cantidadList() {
        return Arrays.asList("100","200", "300","1000");
    }
}