import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.example.DigitalWallet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class Deposito {

    @ParameterizedTest
    @ValueSource(doubles = {10.0, 0.01, 999.99})
    @DisplayName("Atualiza corretamente o saldo para valores válidos")
    void deveDepositarValoresValidos(double amount) {
        DigitalWallet wallet = new DigitalWallet("Test Owner", 100.0);
        wallet.deposit(amount);
        assertEquals(100.0 + amount, wallet.getBalance());
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, -50.0})
    @DisplayName("Lança exceção ao tentar depositar 0 ou valor negativo")
    void deveLancarExcecaoParaDepositoInvalido(double amount) {
        DigitalWallet wallet = new DigitalWallet("Test Owner", 100.0);
        assertThrows(IllegalArgumentException.class, () -> {
            wallet.deposit(amount);
        });
    }
}
