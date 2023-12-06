import java.io.*;
// ESERCIZIO 4.1
public class Valutatore
{
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    public Valutatore(Lexer l, BufferedReader br)
    {
        lex = l;
        pbr = br;
        move();
    }

    void move() {
        look = lex.lexical_scan(pbr);
        System.out.println("token = " + look);
    }

    void error(String s) {
        throw new Error("near line " + lex.line + ": " + s);
    }

    void match(int t) {
        if (look.tag == t)
        {
            if (look.tag != Tag.EOF)
                move(); //Se non e' finito il testo prendo il token dopo
        }
        else error("syntax error");
    }

    public void start()
    {
        int expr_val;
        //Parte dell'insieme guida
        switch (look.tag)
        {
            case Tag.NUM:
            case '(':
                expr_val = expr();
                match(Tag.EOF);
                System.out.println(expr_val);
                break;
            case Tag.EOF:
                break;
            default:
                error("Errore in start");
        }
    }

    private int expr()
    {
        int term_val, exprp_val = 0;
        switch (look.tag)
        {
            case Tag.NUM:
            case '(':
                term_val = term();
                //exprp_i = term_val
                //exprp_val = exprp(exprp_i)
                //expr_val = exprp_val
                exprp_val = exprp(term_val);
                break;
            default:
                error("Errore in expr");
        }
        //return expr_val
        return exprp_val; //Simbolo non terminale sintetizzato
    }

    private int exprp(int exprp_i)//Come parametro l'attributo ereditato
    {
        int term_val, exprp_val = 0;
        switch (look.tag)
        {
            case '+':
                match('+');
                term_val = term();
                //exprp1_i = exprp_i + term_val
                //exprp1_val = exprp(exprp1_i)
                //exprp_val = exprp1_val
                exprp_val = exprp(exprp_i + term_val);
                break;
            case '-':
                match('-');
                term_val = term();
                exprp_val = exprp(exprp_i - term_val);
                break;
            case Tag.EOF: //exprp --> epsilon
            case ')':
                exprp_val = exprp_i;
                break;
            default:
                error("Errore in exprp");
        }
        return exprp_val; //Come return l'attributo sintetizzato
    }

    private int term()
    {
        int fact_val, termp_val = 0;
        switch (look.tag)
        {
            case Tag.NUM:
            case '(': // term --> fact termp
                fact_val = fact();
                termp_val = termp(fact_val);
                break;
            default:
                error("Errore in term");
        }
        return termp_val;
    }

    private int termp(int termp_i)
    {
        int fact_val, termp_val = 0;
        switch(look.tag)
        {
            case '*': //termp --> * fact termp
                match('*');
                fact_val = fact();
                termp_val = (termp_i * fact_val);
                break;
            case '/': //termp --> / fact termp
                match('/');
                fact_val = fact();
                termp_val = termp(termp_i / fact_val);
                break;
            case Tag.EOF: // termp --> epsilon
            case '+':
            case '-':
            case ')':
                termp_val = termp_i;
                break;
            default:
                error("Errore in termp");
        }
        return termp_val;
    }

    private int fact()
    {
        int fact_val = 0;
        switch(look.tag)
        {
            case Tag.NUM:
                if (look instanceof NumberTok) //Devo fare il controllo prima del downcast
                    fact_val = ((NumberTok)look).lexenum;
                match(Tag.NUM);
                break;
            case'(':
                match('(');
                fact_val = expr();
                match(')');
                break;
            default:
                error("Errore in fact");
        }
        return fact_val;
    }

    public static void main(String[] args)
    {
        Lexer lex = new Lexer();
        String path = "test.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Valutatore valutatore = new Valutatore(lex, br);
            valutatore.start();
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}