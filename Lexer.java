import java.io.*;
import java.util.*;

public class Lexer
{
    public static int line = 1;
    private char peek = ' ';

    private void readch(BufferedReader br) {
        try {
            peek = (char) br.read();
        } catch (IOException exc) {
            peek = (char) -1; // ERROR
        }
    }

    public Token lexical_scan(BufferedReader br)
    {
        while (peek == ' ' || peek == '\t' || peek == '\n'  || peek == '\r')
        {
            if (peek == '\n')
                line++;
            readch(br);
        }

        switch (peek)
        {
            case '!':
                peek = ' ';
                return Token.not;
            case '(':
                peek = ' ';
                return Token.lpt;
            case ')':
                peek = ' ';
                return Token.rpt;
            case '{':
                peek = ' ';
                return Token.lpg;
            case '}':
                peek = ' ';
                return Token.rpg;
            case '+':
                peek = ' ';
                return Token.plus;
            case '-':
                peek = ' ';
                return Token.minus;
            case '*':
                peek = ' ';
                return Token.mult;
            case '/':
                peek = ' ';
                return Token.div;
            case ';':
                peek = ' ';
                return Token.semicolon;

            case '&':
                readch(br);
                if (peek == '&')
                {
                    peek = ' ';
                    return Word.and;
                } else {
                    System.err.println("Erroneous character"
                            + " after & : "  + peek );
                    return null;
                }
            case '|':
                readch(br);
                if (peek == '|')
                {
                    peek = ' ';
                    return Word.or;
                } else {
                    System.err.println("Erroneous character"
                            + " after | : "  + peek );
                    return null;
                }
            case '<':
                readch(br);
                if (peek == ' ')
                    return Word.lt;
                else if (peek == '=')
                {
                    peek = ' ';
                    return Word.le;
                } else {
                    System.err.println("Erroneous character"
                            + " after < : "  + peek );
                    return null;
                }
            case '>':
                readch(br);
                if (peek == ' ')
                    return Word.gt;
                else if (peek == '=')
                {
                    peek = ' ';
                    return Word.ge;
                } else {
                    System.err.println("Erroneous character"
                            + " after > : "  + peek );
                    return null;
                }
            case '=':
                readch(br);
                if (peek == ' ')
                    return Token.assign;
                else if (peek == '=')
                {
                    peek = ' ';
                    return Word.eq;
                } else {
                    System.err.println("Erroneous character"
                            + " after = : "  + peek );
                    return null;
                }

            case (char)-1:
                return new Token(Tag.EOF);

            default:

                if (Character.isLetter(peek)) {
                    String raccogli = "";
                    while(Character.isLetter(peek) || Character.isDigit(peek))
                    {
                        raccogli = raccogli + peek;
                        readch(br);
                    }
                    Token token = identifyWord(raccogli);
                    if (token == null)
                        return new Word(Tag.ID, raccogli);
                        //Identificatore
                    else
                        return token;
                    //Parola chiave
                    // ... gestire il caso degli identificatori e delle parole chiave //
                } else if (Character.isDigit(peek)) {
                    String numero = "";
                    while(Character.isDigit(peek))
                    {
                        numero = numero + peek;
                        readch(br);
                    }
                    return new NumberTok(Tag.NUM, numero);
                    // ... gestire il caso dei numeri ... //
                } else {
                    System.err.println("Erroneous character: "
                            + peek );
                    return null;
                }
        }
    }

    private Token identifyWord(String s) {
        if(s.equals(Word.casetok.lexeme))
            return Word.casetok;
        else if(s.equals(Word.dotok.lexeme))
            return Word.dotok;
        else if(s.equals(Word.elsetok.lexeme))
            return Word.elsetok;
        else if(s.equals(Word.then.lexeme))
            return Word.then;
        else if(s.equals(Word.when.lexeme))
            return Word.when;
        else if(s.equals(Word.whiletok.lexeme))
            return Word.whiletok;
        return null;
    }

    public static void main(String[] args)
    {
        Lexer lex = new Lexer();
        String path = "input1.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Token tok;
            do
            {
                tok = lex.lexical_scan(br);
                System.out.println("Scan: " + tok);
            } while (tok.tag != Tag.EOF);
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}
