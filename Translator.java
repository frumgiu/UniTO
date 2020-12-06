import java.io.*;
// da CFG a SDD della grammatica 3.2
// lnext --> attributo ereditato
// lcode --> attributo sintetizzato
//Entrambi sono indirizzi di salto

public class Translator
{
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;
    
    SymbolTable st = new SymbolTable();
    CodeGenerator code = new CodeGenerator();
    int count = 0; //La uso per stabilire gli indirizzi degli ID

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
        switch (look.tag)
        {
            case '{':
            case Tag.ASSIGN:
            case Tag.PRINT:
            case Tag.READ:
            case Tag.COND:
            case Tag.WHILE:
                int lnext_prog = code.newLabel();
                statlist(lnext_prog);
                code.emitLabel(lnext_prog);
                match(Tag.EOF);
                try {
                    code.toJasmin();
                }
                catch(java.io.IOException e) {
                    System.out.println("IO error\n");
                }
                break;
            default:
                error("Errore in prog");
        }
    }
    private void statlist(int next)
    {
        switch (look.tag)
        {
            case '{':
            case Tag.ASSIGN:
            case Tag.PRINT:
            case Tag.READ:
            case Tag.COND:
            case Tag.WHILE:
                stat(next);
                code.emitLabel(next);
                statlistp(next);
                break;
            default:
                error("Errore in statlist");
        }
    }
    private void statlistp(int next)
    {
        switch(look.tag)
        {
            case ';':
                match(';');
                stat(next);
                next = code.newLabel();
                statlistp(next);
                break;
            case '}':
            case Tag.EOF:
                break;
            default:
                error("Errore in statlistp");
        }
    }
    public void stat(  int next  )
    {
        switch(look.tag)
        {
            case Tag.ASSIGN:
                match(Tag.ASSIGN);
                int id_addr = st.lookupAddress(((Word)look).lexeme);
                if (id_addr == -1)
                {
                    id_addr = count;
                    st.insert(((Word)look).lexeme, count++);
                }
                match(Tag.ID);
                expr();
                code.emit(OpCode.istore, id_addr);
                break;
            case Tag.PRINT:
                match(Tag.PRINT);
                match('(');
                exprlist();
                match(')');
                code.emit(OpCode.invokestatic, 1);
                code.emit(OpCode.GOto, next);
                break;
            case Tag.READ: //Gia' fatto
                match(Tag.READ);
                match('(');
                if (look.tag == Tag.ID)
                {
                    id_addr = st.lookupAddress(((Word) look).lexeme);
                    if (id_addr == -1) //Non e' caricata nella symbol table
                    {
                        id_addr = count;
                        st.insert(((Word)look).lexeme, count++); //Faccio un downcast di look per accedere al suo lexeme
                    }                    
                    match(Tag.ID);
                    match(')');
                    code.emit(OpCode.invokestatic,0); //!= 1 e' la read
                    code.emit(OpCode.istore, id_addr);
                    code.emit(OpCode.GOto, next);
                }
                else // Non rispetta le regole per essere un identificatore
                    error("Error in grammar (stat) after read( with " + look);
                break;
            case Tag.COND:
                match(Tag.COND);
                int wlNext = next;
                whenlist();
               // code.emitLabel(trueNext);
               // int falseNext = code.newLabel();
               // code.emitLabel(falseNext);
                match(Tag.ELSE);
                //stat(/*falseNext*/);
                break;
            case Tag.WHILE:
                match(Tag.WHILE);
                match('(');
                bexpr();
                match(')');
                //stat(/*next*/);
                break;
            case '{':
                match('{');
                next = code.newLabel();
                statlist(next);
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
                //stat(/*next*/);
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
                Word relop = (Word)look;
                match(Tag.RELOP);
                expr();
                expr();
                switch(relop.lexeme)
                {
                    case "==":
                        int trueL = code.newLabel();
                        code.emit(OpCode.if_icmpeq, trueL);
                        int falseL = code.newLabel();
                        code.emit(OpCode.GOto, falseL);
                        break;
                    case "<>":
                        code.emit(OpCode.if_icmpne, code.label);
                        break;
                        // >=
                    case "<":
                        code.emit(OpCode.if_icmplt, code.label);
                        break;
                        // >
                     case "<=":
                        code.emit(OpCode.if_icmple, code.label);
                        // <=
                    case ">":
                        code.emit(OpCode.if_icmpgt, code.label);
                        break;
                        // <
                    case ">=":
                        code.emit(OpCode.if_icmpge, code.label);
                        break;
                }
                break;
            default:
                error("Errore in bexpr");
        }
    }
    private void expr( /* completare */ )
    {
        switch(look.tag)
        {
            case '+':
                match('+');
                match('(');
                exprlist();
                match(')');
                code.emit(OpCode.iadd);
                break;
            case '*':
                match('*');
                match('(');
                exprlist();
                match(')');
                code.emit(OpCode.imul);
                break;
            case '-':
                match('-');
                expr();
                expr();
                code.emit(OpCode.isub);
                break;
            case '/':
                match('/');
                expr();
                expr();
                code.emit(OpCode.idiv);
                break;
            case Tag.NUM:
                code.emit(OpCode.ldc, ((NumberTok)look).lexenum); //Per prendere il valore numerico di look da caricare per l'assembly
                match(Tag.NUM);
                break;
            case Tag.ID:
                int id_addr = st.lookupAddress(((Word)look).lexeme);
                if (id_addr == -1)
                {
                    id_addr = count;
                    st.insert(((Word)look).lexeme, count++);
                }
                match(Tag.ID);
                code.emit(OpCode.iload, id_addr);
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

    public static void main (String[] args)
    {
        Lexer lex = new Lexer();
        String path = "testParserProg.txt";
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Translator translator = new Translator(lex, br);
            translator.prog();
            br.close();
        }
        catch (IOException e) {e.printStackTrace();}
    }
}

