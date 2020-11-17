import java.io.*;
//OK: 2.1, 2.2, 2.3

public class Lexer
{
    public static int line = 1;
    private char peek = ' ';
    /* il controllo che uso per i commenti mi fa saltare un carattere, che puo' compromettere la riuscita del parser
    dopo, se c'e' un / con un numero SENZA SPAZIO tra i due */
    //RISOLTO

//Legge un carattere dal testo di input, caricato nel BufferReader
    private void readch(BufferedReader br)
    {
        try {
            peek = (char) br.read();
        } catch (IOException exc) {
            peek = (char) -1; // ERROR
        }
    }
//Tokenizza il carattere letto
    public Token lexical_scan(BufferedReader br)
    {
        while (peek == ' ' || peek == '\t' || peek == '\n'  || peek == '\r' || peek == '/')
        {
            if (peek == '\n')
                line++;
           if (peek == '/')
            {
                readch(br);
                if (peek == '/') //Salto tutta la riga
                {
                    while(peek != '\n' && peek != (char)-1) //Consumo caratteri finche' non finisce la riga o il testo
                    {
                        readch(br);
                    }
                }
                else if (peek == '*') //Salto fino a '*/'
                {
                    if(!commento(br)){
                        System.err.println("Il commento non si chiude dopo averlo aperto con /*");
                        return null;
                    }
                    peek = ' ';
                }
                else
                {
                   // peek = ' '; Va tolto, senno' mi fa saltare il carattere subito dopo il diviso
                    return Token.div;
                }
            }
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
                    System.err.println("Erroneous character" + " after & : "  + peek );
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

                if (Character.isLetter(peek) || peek == '_')
                {
                    String raccogli = "";
                    while(Character.isLetter(peek) || Character.isDigit(peek) || peek == '_')
                    {
                        raccogli = raccogli + peek;
                        readch(br);
                    }
                    Token token = keywordWord(raccogli);
                    if (token == null) //Identificatore
                    {
                        if (wordID(raccogli)) //True, ho un ID valido secondo le nuove direttive
                            return new Word(Tag.ID, raccogli);
                        else
                        {
                            System.err.println("Non e' un identificatore: " + raccogli );
                            return null;
                        }
                    }

                    else //Parola chiave
                        return token;
                }
                else if (Character.isDigit(peek))
                {
                    if(peek == '0') //0 non puo' avere numeri che lo seguono
                    {
                        readch(br);
                        if(Character.isDigit(peek))
                        {
                            System.err.println("Lo 0 non puo' essere seguito da altri numeri: " + peek );
                            return null;
                        }
                        else
                            return new NumberTok(Tag.NUM, 0);
                    }
                    else
                    {
                        String numero = "";
                        while(Character.isDigit(peek))
                        {
                            numero = numero + peek;
                            readch(br);
                        }
                        return new NumberTok(Tag.NUM, Integer.parseInt(numero));
                    }
                }
                else
                {
                    System.err.println("Erroneous character: " + peek );
                    return null;
                }
        }
    }

//Esercizio 1.2
//Se e' un identificatore valido
    private boolean wordID(String s)
    {
        int state = 0;
        for(int i = 0; i < s.length() && state >= 0; i++)
        {
            char valore = s.charAt(i);
            switch (state)
            {
                case 0:
                    if ((valore == '_'))
                        state = 1;
                    else if (Character.isLetter(valore))
                        state = 2;
                    else
                        state = -1;
                    break;
                case 1:
                    if (valore == '_')
                        state = 1;
                    else if (Character.isDigit(valore) || Character.isLetter(valore))
                        state = 2;
                    else
                        state = -1;
                    break;
                case 2:
                    if ((valore == '_') || Character.isDigit(valore) || Character.isLetter(valore))
                        state = 2;
                    else
                        state = -1;
                    break;
            }
        }
        return state == 2;
    }
//Usato esrcizio 1.9
//Viene chiamato solo nel caso legga /*
//Consumo caratteri finche' non si chiude il commento o finisce il testo (commento non viene chiuso)
    private boolean commento(BufferedReader br)
    {
        int stato = 0;
        while(peek != (char)-1 && stato != 2)
        {
            switch (stato)
            {
                case 0:
                    if (peek == '*')
                        stato = 1;
                    else
                        stato = 0;
                    break;
                case 1:
                    if (peek == '/')
                        stato = 2;
                    else if (peek == '*')
                        stato = 1;
                    else
                        stato = 0;
                    break;
            }
            readch(br);
        }
        return stato == 2;
    }
//Se la stringa raccolta e' una parola chiave riconoscibile
    private Token keywordWord(String s)
    {
        switch (s)
        {
            case "cond":
                return Word.cond;
            case "dotok":
                return Word.dotok;
            case "elsetok":
                return Word.elsetok;
            case "then":
                return Word.then;
            case "when":
                return Word.when;
            case "whiletok":
                return Word.whiletok;
            case "print":
                return Word.print;
            case "read":
                return Word.read;
            default:
                return null;
        }
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
