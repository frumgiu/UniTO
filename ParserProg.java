import java.io.*;

public class ParserProg
{
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    public ParserProg(Lexer l, BufferedReader br) //Costruttore
    {
        lex = l;
        pbr = br;
        move();
    }

    void move()//look prende un token alla volta
    {
        look = lex.lexical_scan(pbr);
        System.out.println("token = " + look);
    }
    //Per stampare messaggio d'errore
    void error(String s) {
        throw new Error("near line " + lex.line + ": " + s);
    }
    //Verifica che il Token che ho sia quello giusto
    void match(int t)
    {
        if (look.tag == t)
        {
            if (look.tag != Tag.EOF)
                move(); //Se non e' finito il testo prendo il token dopo
        }
        else error("syntax error");
    }
    //Primo simbolo della grammatica
    public void prog()
    {
        switch(look.tag)
        {
            case '{':
            case Tag.SEQ:
            case Tag.PRINT:
            case Tag.READ:
            case Tag.COND:
            case Tag.WHILE:
                statlist();
                match(Tag.EOF);
                break;
            default:
                error("Errore in prog");

        }
    }
    private void statlist()
    {
        switch (look.tag)
        {
            case '{':
            case '=':
            case Tag.PRINT:
            case Tag.READ:
            case Tag.COND:
            case Tag.WHILE:
                stat();
                statlistp();
                break;
            default:
                error("Errore in statlist");
        }
    }
    private void statlistp()
    {
        switch(look.tag)
        {
            case ';':
                match(';');
                stat();
                statlistp();
                break;
            case '}':
            case Tag.EOF:
                break;
            default:
                error("Errore in statlistp");
        }
    }
    private void stat()
    {
        switch (look.tag)
        {
            case '=':
                match('=');
                match(Tag.ID);
                expr();
                break;
            case Tag.PRINT:
                match(Tag.PRINT);
                match('(');
                exprlist();
                match(')');
                break;
            case Tag.READ:
                match(Tag.READ);
                match('(');
                match(Tag.ID);
                match(')');
                break;
            case Tag.COND:
                match(Tag.COND);
                whenlist();
                match(Tag.ELSE);
                stat();
                break;
            case Tag.WHILE:
                match(Tag.WHILE);
                match('(');
                bexpr();
                match(')');
                stat();
                break;
            case '{':
                match('{');
                statlist();
                match('}');
                break;
            default:
                error("Errore in stat");
        }
    }
    private void whenlist()
    {
        switch(look.tag)
        {
            case Tag.WHEN:
                whenitem();
                whenlistp();
                break;
            default:
                error("Errore in whenlist");
        }
    }
    private void whenlistp()
    {
        switch(look.tag)
        {
            case Tag.WHEN:
                whenitem();
                whenlistp();
                break;
            case Tag.ELSE:
                break;
            default:
                error("Errore in whenlistp");
        }
    }
    private void whenitem()
    {
        switch(look.tag)
        {
            case Tag.WHEN:
                match(Tag.WHEN);
                match('(');
                bexpr();
                match(')');
                match(Tag.DO);
                stat();
                break;
            default:
                error("Errore in whenlist");
        }
    }
    private void bexpr()
    {
        switch(look.tag)
        {
            case Tag.RELOP:
                match(Tag.RELOP);
                expr();
                expr();
                break;
            default:
                error("Errore in bexpr");
        }
    }
    private void expr()
    {
        switch(look.tag)
        {
            case '+':
                match('+');
                match('(');
                exprlist();
                match(')');
                break;
            case '*':
                match('*');
                match('(');
                exprlist();
                match(')');
                break;
            case '-':
                match('-');
                expr();
                expr();
                break;
            case '/':
                match('/');
                expr();
                expr();
                break;
            case Tag.NUM:
                match(Tag.NUM);
                break;
            case Tag.ID:
                match(Tag.ID);
                break;
            default:
                error("Errore in expr");
        }
    }
    private void exprlist()
    {
        switch(look.tag)
        {
            case '+':
            case '-':
            case '*':
            case '/':
            case Tag.NUM:
            case Tag.ID:
                expr();
                exprlistp();
                break;
            default:
                error("Errore in exprlist");
        }
    }
    private void exprlistp()
    {
        switch(look.tag)
        {
            case '+':
            case '-':
            case '*':
            case '/':
            case Tag.NUM:
            case Tag.ID:
                expr();
                exprlistp();
                break;
            case ')':
                break;
            default:
                error("Errore in exprlistp");
        }
    }

    public static void main(String[] args)
    {
        Lexer lex = new Lexer();
        String path = "testParserProg.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            ParserProg parser = new ParserProg(lex, br);
            parser.prog();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}
