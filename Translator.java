import java.io.*;
// da CFG a SDD della grammatica 3.2

public class Translator
{
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;
    
    SymbolTable st = new SymbolTable();
    CodeGenerator code = new CodeGenerator();
    int count = 0;

    public Translator(Lexer l, BufferedReader br)
    {
        lex = l;
        pbr = br;
        move();
    }
    void move()
    {
        look = lex.lexical_scan(pbr);
        System.out.println("token = " + look);
    }
    void error(String s) { throw new Error("near line " + lex.line + ": " + s); }
    void match(int t) {
        if (look.tag == t)
        {
            if (look.tag != Tag.EOF)
                move(); //Se non e' finito il testo prendo il token dopo
        }
        else error("syntax error");
    }

    public void prog()
    {
	// ... completare ...
        int lnext_prog = code.newLabel();
        statlist(lnext_prog);
        code.emitLabel(lnext_prog);
        match(Tag.EOF);
        try {
        	code.toJasmin();
        }
        catch(java.io.IOException e) {
        	System.out.println("IO error\n");
        };
	// ... completare ...
    }
    private void statlist()
    {
        switch (look.tag)
        {
            case '{':
            case Tag.ASSIGN:
            case Tag.PRINT:
            case Tag.READ:
            case Tag.CASE:
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
    public void stat( /* completare */ )
    {
        switch(look.tag)
        {
	// ... completare ...
            case Tag.READ:
                match(Tag.READ);
                match('(');
                if (look.tag == Tag.ID)
                {
                    int id_addr = st.lookupAddress(((Word)look).lexeme);
                    if (id_addr == -1)
                    {
                        id_addr = count;
                        st.insert(((Word)look).lexeme, count++); //Faccio un downcast di look per accedere al suo lexeme
                    }                    
                    match(Tag.ID);
                    match(')');
                    code.emit(OpCode.invokestatic,0);
                    code.emit(OpCode.istore, id_addr);
                }
                else // Non rispetta le regole per essere un identificatore
                    error("Error in grammar (stat) after read( with " + look);
                break;
	// ... completare ...
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
    private void expr( /* completare */ )
    {
        switch(look.tag) {
	// ... completare ...
            case '-':
                match('-');
                expr();
                expr();
                code.emit(OpCode.isub);
                break;
	// ... completare ...
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

    public static void main (String[] args)
    {
        Lexer lex = new Lexer();
        String path = "testParserProg";
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Translator translator = new Translator(lex, br);
            translator.prog();
            br.close();
        }
        catch (IOException e) {e.printStackTrace();}
    }
}

