import java.io.*;

public class Translator {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    SymbolTable st = new SymbolTable();
    CodeGenerator code = new CodeGenerator();
    int count=0;

    public Translator(Lexer l, BufferedReader br) {
        lex = l;
        pbr = br;
        move();
    }
    void move()
    {
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

    public void prog()
    {
        switch(look.tag)
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
                } catch (java.io.IOException e) {
                    System.out.println("IO error\n");
                }
                break;
            default:
                error("Errore in prog");
        }
    }
    private void statlist(int sl_next)
    {
        switch (look.tag)
        {
            case '{':
            case Tag.ASSIGN:
            case Tag.PRINT:
            case Tag.READ:
            case Tag.COND:
            case Tag.WHILE:
                int s_next = code.newLabel();
                stat(sl_next);
                //code.emitLabel(s_next);
                statlistp(s_next);

                break;
            default:
                error("Errore in statlist");
        }
    }
    //slp_next si puo' togliere, non serve a nulla
    private void statlistp(int s_next)
    {
        switch(look.tag)
        {
            case ';':
                match(';');
                //int s_next = code.newLabel();
                stat(s_next); // s_next = newLabel()
                code.emitLabel(s_next);
                statlistp(code.newLabel());
                break;
            case '}':
            case Tag.EOF:
                break;
            default:
                error("Errore in statlistp");
        }
    }
    public void stat(int s_next)
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
                break;
            case Tag.READ:
                match(Tag.READ);
                match('(');
                if (look.tag==Tag.ID)
                {
                    id_addr = st.lookupAddress(((Word)look).lexeme);
                    if (id_addr==-1) //Non e' caricata nella symbol table
                    {
                        id_addr = count;
                        st.insert(((Word)look).lexeme,count++);
                    }
                    match(Tag.ID);
                    match(')');
                    code.emit(OpCode.invokestatic,0);//!= 1 e' la read
                    code.emit(OpCode.istore,id_addr);
                }
                else // Non rispetta le regole per essere un identificatore
                    error("Error in grammar (stat) after read( with " + look);
                break;
            case Tag.COND:
                match(Tag.COND);
                //int wl_true = s_next;
                int wl_false = code.newLabel();
                whenlist(/*s_next,*/ s_next, wl_false);
                match(Tag.ELSE);
                code.emitLabel(wl_false);
                s_next = wl_false;
                stat(s_next);
                break;
            case Tag.WHILE:
                match(Tag.WHILE);
                match('(');
                int cond = code.newLabel(); //Mi permette di tornare indietro
                int be_true = code.newLabel();
                int be_false = s_next; //Se e' false continuo il codice
                code.emitLabel(cond); //Sn label
                bexpr(be_true, be_false);
                code.emitLabel(be_true);
                match(')');
                stat(s_next);
                code.emit(OpCode.GOto, cond);
                break;
            case '{':
                match('{');
                statlist(s_next);
                match('}');
                break;
            case '}':
                break;
            default:
                error("Errore in stat");
        }
     }
    private void whenlist(/*int wl_next,*/ int wl_true, int wl_false)
    {
        switch(look.tag)
        {
            case Tag.WHEN:
                //int wi_next = code.newLabel();
                //int wi_true = wl_true;
                //int wi_false = wl_false;
                whenitem(/*wl_next,*/ wl_true, wl_false);
                code.emitLabel(wl_true);
                //int wlp_next = wl_next;
                //int wlp_true = wl_true;
                //int wlp_false = wl_false;
                whenlistp(/*wl_next,*/ wl_true, wl_false);
                break;
            default:
                error("Errore in whenlist");
        }
    }
    private void whenlistp(/*int wlp_next,*/ int wlp_true, int wlp_false)
    {
        switch(look.tag)
        {
            case Tag.WHEN:
                int wi_next = code.newLabel();
                //int wi_true = wlp_true;
                //int wi_false = wlp_false;
                whenitem(/*wi_next,*/ wlp_true, wlp_false);
                code.emitLabel(wi_next);
                whenlistp(/*wlp_next,*/ wlp_true, wlp_false);
                break;
            case Tag.ELSE:
                break;
            default:
                error("Errore in whenlistp");
        }
    }
    private void whenitem(/*int wi_next,*/ int wi_true, int wi_false)
    {
        switch(look.tag)
        {
            case Tag.WHEN:
                match(Tag.WHEN);
                match('(');
                //int be_true = wi_true;
                //int be_false = wi_false;
                bexpr(wi_true, wi_false);
                match(')');
                match(Tag.DO);
                code.emitLabel(wi_true);
                stat(wi_true);
                break;
            default:
                error("Errore in whenlist");
        }
    }
    private void bexpr(int be_true, int be_false)
    {
        switch(look.tag)
        {
            case Tag.RELOP:
                Word relop = (Word) look; //Mi serve per capire che simbolo RELOP c'e'
                match(Tag.RELOP);
                expr();
                expr();
                switch(relop.lexeme)
                {
                    case "==":
                        code.emit(OpCode.if_icmpeq, be_true);
                        code.emit(OpCode.GOto, be_false);
                        break;
                    case "<>":
                        code.emit(OpCode.if_icmpne, be_true);
                        code.emit(OpCode.GOto, be_false);
                        break;
                    case "<":
                        code.emit(OpCode.if_icmplt, be_true);
                        code.emit(OpCode.GOto, be_false);
                        break;
                    case "<=":
                        code.emit(OpCode.if_icmple, be_true);
                        code.emit(OpCode.GOto, be_false);
                        break;
                    case ">":
                        code.emit(OpCode.if_icmpgt, be_true);
                        code.emit(OpCode.GOto, be_false);
                        break;
                    case ">=":
                        code.emit(OpCode.if_icmpge, be_true);
                        code.emit(OpCode.GOto, be_false);
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

