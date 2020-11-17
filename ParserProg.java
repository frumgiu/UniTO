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
    }
    private void statlist()
    {

    }
    private void statlistp()
    {

    }
    private void stat()
    {

    }
    private void whenlist()
    {}
    private void whenlistp()
    {}
    private void whenitem()
    {

    }
    private void bexpr()
    {}
    private void expr()
    {
    }
    private void exprlist()
    {}
    private void exprlistp()
    {}

    public static void main(String[] args)
    {
        Lexer lex = new Lexer();
        String path = "input1.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            ParserProg parser = new ParserProg(lex, br);
            parser.prog();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}
