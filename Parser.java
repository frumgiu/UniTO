import java.io.*;
//NOTAZIONE FORMA INFISSA
//Es. 2+2
//Grammatica LL(1)
//Varibili: start, expr, exprp, term, termp, fact
//Terminali: 0,...,9, +, -, *, /, (, ) (3.1)
//Produzioni: in ogno metodo c'e' la sua produzione

//Problema lexer spazio con il /. RISOLTO
public class Parser
{
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    public Parser(Lexer l, BufferedReader br) //Costruttore
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
    public void start()
    {
        switch(look.tag)
        {
            case Tag.EOF:
            case Tag.NUM:
            case '(':
                expr();
                match(Tag.EOF);
                break;
            default:
                error("Errore in start");
        }
    }
    //Sotto seguono un metodo per ogni variabile della grammatica
    private void expr()
    {
        switch(look.tag)
        {
            case Tag.NUM:
            case '(': //expr --> term exprp
                term();
                exprp();
                break;
            default:
                error("Errore in expr");
        }
    }
    //Questa variabile e' annullabile, NULL(exprp)
    private void exprp()
    {
        switch(look.tag)
        {
            case '+':  //exprp --> + temp exprp
                match('+');
                term();
                exprp();
                break;
            case '-': //exprp --> - temp exprp
                match('-');
                term();
                exprp();
                break;
            case Tag.EOF: //exprp --> epsilon
            case ')':
                break;
            default:
                error("Errore in exprp");
        }
    }
    private void term()
    {
        switch (look.tag)
        {
            case Tag.NUM:
            case '(': // term --> fact termp
                fact();
                termp();
                break;
            default:
                error("Errore in term");
        }
    }
    //Questa variabile e' annullabile, NULL(termp)
    private void termp()
    {
        switch(look.tag)
        {
            case '*': //termp --> * fact termp
                match('*');
                fact();
                termp();
                break;
            case '/': //termp --> / fact termp
                match('/');
                fact();
                termp();
                break;
            case Tag.EOF: // termp --> epsilon
            case '+':
            case '-':
            case ')':
                break;
            default:
                error("Errore in termp");
        }
    }
    private void fact()
    {
        switch(look.tag)
        {
            case Tag.NUM:
                match(Tag.NUM);
                break;
            case'(':
                match('(');
                expr();
                match(')');
                break;
            default:
                error("Errore in fact");
        }
    }

    public static void main(String[] args)
    {
        Lexer lex = new Lexer();
        String path = "test.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Parser parser = new Parser(lex, br);
            parser.start();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}