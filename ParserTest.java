import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    private Lexer lex;

    @BeforeEach
    void beforeEach(){
        lex = new Lexer();

    }

    @ParameterizedTest
    @ValueSource(strings = {"(2+3)/2", "(2/3)+2", "(2-3)", "(2)+(3)", "2+3*1", "((2*1) - (3+6))"})
    void testok(String input){
        String path = "input1.txt"; // il percorso del file da leggere
        BufferedReader br = new BufferedReader(new StringReader(input));
        Parser parser = new Parser(lex, br);
        parser.start();
    }

    @ParameterizedTest
    @ValueSource(strings = {"2+(+", "34-("})
    void testNotOkExpr(String input) {
        String path = "input1.txt"; // il percorso del file da leggere
        BufferedReader br = new BufferedReader(new StringReader(input));
        Parser parser = new Parser(lex, br);
        try{
            parser.start(); //Controllare debug
        } catch(Error e) {
            Assertions.assertEquals("near line 1: Errore in expr", e.getMessage());
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"2+1("})
    void testNotOkTermp(String input) {
        String path = "input1.txt"; // il percorso del file da leggere
        BufferedReader br = new BufferedReader(new StringReader(input));
        Parser parser = new Parser(lex, br);
        try{
            parser.start(); //Controllare debug
        } catch(Error e) {
            Assertions.assertEquals("near line 1: Errore in termp", e.getMessage());
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"5+)"})
    void testNotOkTerm(String input) {
        String path = "input1.txt"; // il percorso del file da leggere
        BufferedReader br = new BufferedReader(new StringReader(input));
        Parser parser = new Parser(lex, br);
        try{
            parser.start();
        } catch(Error e) {
            Assertions.assertEquals("near line 1: Errore in term", e.getMessage());
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {")2", "+12"})
    void testNotOkStart(String input) {
        String path = "input1.txt"; // il percorso del file da leggere
        BufferedReader br = new BufferedReader(new StringReader(input));
        Parser parser = new Parser(lex, br);
        try{
            parser.start();
        } catch(Error e) {
            Assertions.assertEquals("near line 1: Errore in start", e.getMessage());
        }
    }
}