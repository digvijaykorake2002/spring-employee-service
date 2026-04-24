package com.moduleseven.TestingApp;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
class TestingAppApplicationTests {

	@BeforeEach
	void methodOne() {
        log.info("method one run before each methods..");
	}

    @AfterEach
    void methodTwo(){
        log.info("Method Two run after each method");
    }

    @BeforeAll
    static void methodThree(){
        log.info("Method Three run Before All Methods Present in Test Class");
    }

    @AfterAll
    static void methodFour(){
        log.info("Method Four run After All Methods Present in Test Class");
    }

    @Test
//  @Disabled
    void testDivideTwoNumbers_whenDenominaterIsZero_ThenArithmeticException(){

        int a = 6;
        int b = 0;

        assertThatThrownBy(()-> divideTwoNumbers(a,b))
                .isInstanceOf(ArithmeticException.class)
                .hasMessage("Tried to divide by zero");
//
//        double result = divideTwoNumbers(a,b);

//        Explanation: JUnit assertions do not support method chaining. If we want to perform multiple checks, we need to write separate assertions each time.
//        Assertions.assertEquals(6,result);
//        Assertions.


//        Explanation: Assertions comes from the AssertJ library and supports chaining methods to validate data in multiple ways.
//          Assertions.assertThat(result)
//                  .isEqualTo(8)
//                  .isCloseTo(9,Offset.offset(1));

//          Assertions.assertThat("Apple")
//                  .startsWith("App")
//                  .endsWith("le")
//                  .hasSize(5);


}

    @Test
//  @DisplayName("DisplayTestNameOne")
    void testNumberTwo(){
        log.info("Test Number Two Is Running.....");
    }


    int addTwoNumbers(int a, int b){
        return a+b;
    }

    int divideTwoNumbers(int a, int b){
        try {
            return a / b;
        }
        catch (ArithmeticException e){
            log.info("Arithmetic Exception Occured: "+ e.getLocalizedMessage());
            throw new ArithmeticException("Tried to divide by zero");
        }

    }
}
