import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import com.example.DigitalWallet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

class Estorno {

    private DigitalWallet wallet;

    @BeforeEach
    void setUp() {
        wallet = new DigitalWallet("Test Owner", 100.0);
    }

    static Stream<Arguments> valoresEstorno() {
        return Stream.of(
            Arguments.of(100.0, 10.0, 110.0),
            Arguments.of(0.0, 5.0, 5.0),
            Arguments.of(50.0, 0.01, 50.01)
        );
    }

    @ParameterizedTest
    @MethodSource("valoresEstorno")
    @DisplayName("Estorno válido aumenta o saldo")
    void estornoValidoAumentaOSaldo(double initialBalance, double refundAmount, double expectedBalance) {
        DigitalWallet localWallet = new DigitalWallet("Test Owner", initialBalance);
        localWallet.verify();
        localWallet.unlock();

        assumeTrue(localWallet.isVerified() && !localWallet.isLocked());

        localWallet.refund(refundAmount);
        assertEquals(expectedBalance, localWallet.getBalance(), 0.001);
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, -50.0})
    @DisplayName("Lança exceção ao estornar 0 ou negativo")
    void lancaExcecaoAoEstornarValorInvalido(double amount) {
        wallet.verify();
        assumeTrue(wallet.isVerified() && !wallet.isLocked());

        assertThrows(IllegalArgumentException.class, () -> {
            wallet.refund(amount);
        });
    }

    @Test
    @DisplayName("Lança IllegalStateException se a carteira não estiver verificada")
    void lancaExcecaoSeCarteiraNaoVerificada() {
        assertThrows(IllegalStateException.class, () -> {
            wallet.refund(50.0);
        });
    }

    @Test
    @DisplayName("Lança IllegalStateException se a carteira estiver bloqueada")
    void lancaExcecaoSeCarteiraBloqueada() {
        wallet.verify();
        wallet.lock();
        assertThrows(IllegalStateException.class, () -> {
            wallet.refund(50.0);
        });
    }
}