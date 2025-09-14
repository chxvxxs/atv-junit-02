import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import com.example.DigitalWallet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class Pagamento {

    private DigitalWallet wallet;

    @BeforeEach
    void setUp() {
        wallet = new DigitalWallet("Test Owner", 100.0);
    }

    @ParameterizedTest
    @CsvSource({
        "100.0, 30.0, 70.0, true",
        "50.0, 80.0, 50.0, false",
        "10.0, 10.0, 0.0, true"
    })
    @DisplayName("Testa pagamentos válidos e com saldo insuficiente")
    void pagamentosValidosDebitamSaldoERetornamTrue(double initialBalance, double paymentAmount, double expectedBalance, boolean expectedResult) {
        DigitalWallet localWallet = new DigitalWallet("Test Owner", initialBalance);
        localWallet.verify();
        localWallet.unlock();

        assumeTrue(localWallet.isVerified() && !localWallet.isLocked());

        boolean result = localWallet.pay(paymentAmount);

        assertEquals(expectedResult, result);
        assertEquals(expectedBalance, localWallet.getBalance(), 0.001);
    }

    @Test
    @DisplayName("Saldo insuficiente retorna false e não altera saldo")
    void saldoInsuficienteRetornaFalseENaoAlteraSaldo() {
        wallet.verify();
        assumeTrue(wallet.isVerified() && !wallet.isLocked());

        boolean result = wallet.pay(150.0);

        assertFalse(result);
        assertEquals(100.0, wallet.getBalance());
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, -50.0})
    @DisplayName("Lança exceção para pagamento com valor <= 0")
    void lancaExcecaoParaPagamentoComValorInvalido(double amount) {
        wallet.verify();
        assumeTrue(wallet.isVerified() && !wallet.isLocked());

        assertThrows(IllegalArgumentException.class, () -> {
            wallet.pay(amount);
        });
    }

    @Test
    @DisplayName("Lança IllegalStateException se a carteira não estiver verificada")
    void lancaExcecaoSeCarteiraNaoVerificada() {
        // Por padrão, a carteira começa não verificada
        assertThrows(IllegalStateException.class, () -> {
            wallet.pay(50.0);
        });
    }

    @Test
    @DisplayName("Lança IllegalStateException se a carteira estiver bloqueada")
    void lancaExcecaoSeCarteiraBloqueada() {
        wallet.verify();
        wallet.lock();
        assertThrows(IllegalStateException.class, () -> {
            wallet.pay(50.0);
        });
    }
}