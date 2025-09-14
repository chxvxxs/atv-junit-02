import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.DigitalWallet;

class SaldoInicial {
    @Test
    @DisplayName("Verifica que o saldo inicial é configurado corretamente")
    void deveConfigurarSaldoInicialCorreto() {
        DigitalWallet wallet = new DigitalWallet("Test Owner", 100.0);
        assertEquals(100.0, wallet.getBalance());
    }

    @Test
    @DisplayName("Lança exceção ao tentar criar carteira com saldo inicial negativo")
    void deveLancarExcecaoParaSaldoInicialNegativo() {
        assertThrows(IllegalArgumentException.class, () -> {
            new DigitalWallet("Test Owner", -50.0);
        });
    }
}
