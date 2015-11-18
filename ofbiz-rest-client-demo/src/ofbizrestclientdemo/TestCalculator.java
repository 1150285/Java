package ofbizrestclientdemo;

import static org.junit.Assert.*; 
import org.junit.*;


public class TestCalculator {
	@Test 
	public void testAdd() {
		Calculator calculator = new Calculator();
		double result = calculator.add(10, 50);
		assertEquals(60, result, 0);
	}
	
	@Test
	public void testSub() {
		Calculator calculator = new Calculator();
		double result = calculator.sub(5, 3);
		assertEquals(2, result, 0);
	}
	@Test
	public void testMul() {
		Calculator calculator = new Calculator();
		double result = calculator.mul(5, 3);
	 	assertEquals(15, result, 0);
	}
	@Test
	public void testDiv() {
		Calculator calculator = new Calculator();
		double result = calculator.div(60, 10);
		assertEquals(6, result, 0);
	}
}