import java.io.*;

public class Lexer
{
    public static int line = 1;
    private char peek = ' ';

    private void readch(BufferedReader br)
    {
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
                    System.err.println("Erroneous character" + " after | : "  + peek );
                    return null;
                }
            case '<':
                readch(br);
                if (peek == '=')
                {
                    peek = ' ';
                    return Word.ne;
                }
                else if (peek == '>')
                {
                    readch(br);
                    return Word.le;
                }
                else
                { return Word.lt; }
            case '>':
                readch(br);
                if (peek == '=')
                {
                    readch(br);
                    return Word.ge;
                } else {
                    return Word.gt;
                }
            case '=':
                readch(br);
                if (peek == '=')
                {
                    readch(br);
                    return Word.eq;
                }
                else
                { return Word.assign; }

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
                    Token token = keywordWord(raccogli);
                    if (token == null)
                        return new Word(Tag.ID, raccogli);
                        //Identificatore
                    else
                        return token;
                    //Parola chiave
                } else if (Character.isDigit(peek)) {
                    if(peek == '0') //0 non puo' avere numeri che lo seguono
                    {
                        readch(br);
                        if(Character.isDigit(peek))
                        {
                            System.err.println("Lo 0 non puo' essere seguito da altri numeri: " + peek );
                            return null;
                        }
                        else
                            return new NumberTok(Tag.NUM, "0");
                    }
                    else
                    {
                        String numero = "";
                        while(Character.isDigit(peek))
                        {
                            numero = numero + peek;
                            readch(br);
                        }
                        return new NumberTok(Tag.NUM, numero);
                    }
                } else {
                    System.err.println("Erroneous character: " + peek );
                    return null;
                }
        }
    }

    private Token keywordWord(String s) {
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
        else if(s.equals(Word.print.lexeme))
            return Word.print;
        else if(s.equals(Word.read.lexeme))
            return Word.read;
        else
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
