import java.io.*;

public class Valutatore
{
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    public Valutatore(Lexer l, BufferedReader br) //Costruttore
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

    public void start() {
        int expr_val;

        // ... completare ...

        expr_val = expr();
        match(Tag.EOF);

        System.out.println(expr_val);

        // ... completare ...
    }

    private int expr() {
        int term_val, exprp_val;

        // ... completare ...

        term_val = term();
        exprp_val = exprp(term_val);

        // ... completare ...
        return exprp_val;
    }

    private int exprp(int exprp_i) {
        int term_val, exprp_val;
        switch (look.tag)
        {
            case '+':
                match('+');
                term_val = term();
                exprp_val = exprp(exprp_i + term_val);
                break;

            // ... completare ...
        }
        return exprp_val;
    }

    private int term()
    {
        // ... completare ...
        return 0;
    }

    private int termp(int termp_i)
    {
        // ... completare ...
        return 0;
    }

    private int fact()
    {
        // ... completare ...
        return 0;
    }

    public static void main(String[] args)
    {
        Lexer lex = new Lexer();
        String path = "input1.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Valutatore valutatore = new Valutatore(lex, br);
            valutatore.start();
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}