package fr.s42.numbers;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

// import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.ValueSource;

public class NumberWorkerTest {
	private NumberWorker numberWorker = new NumberWorker();

	@ParameterizedTest
	@CsvFileSource(resources = "/data.csv", numLinesToSkip = 1)
	void testDigitsSumCsvFile(int input, int expect){
		Assertions.assertEquals(expect, numberWorker.digitsSum(input));
	}

	@ParameterizedTest
	@ValueSource(ints = {2, 3, 5, 7, 11, 13})
	void isPrimeForPrimes(int numbers)
	{
		assertTrue(numberWorker.isPrime(numbers));
	}

	@ParameterizedTest
	@ValueSource(ints = {4, 6, 8, 9, 10})
	void isPrimeForNotPrimes(int nb)
	{
		assertFalse(numberWorker.isPrime(nb));
	}

	@ParameterizedTest
	@ValueSource(ints = {-1, 0, -444, -77777})
	void isPrimeForIncorrectNumber(int nb)
	{
		assertThrows(IllegalNumberException.class, ()-> numberWorker.isPrime(nb));
	}

}
