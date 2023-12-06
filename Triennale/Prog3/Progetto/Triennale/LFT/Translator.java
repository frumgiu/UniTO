import java.io.*;
//ESERCIZIO 5.1
public class Translator
{
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    SymbolTable st = new SymbolTable();
    CodeGenerator code = new CodeGenerator();
    int count = 0;

    public Translator(Lexer l, BufferedReader br) {
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
        if (look.tag == t) {
            if (look.tag != Tag.EOF)
                move(); //Se non e' finito il testo prendo il token dopo
        } else error("syntax error");
    }

    public void prog()
    {
        switch (look.tag) {
            case '{':
            case Tag.PRINT:
            case Tag.READ:
            case Tag.COND:
            case Tag.WHILE:
                int lnext_prog = code.newLabel();
                statlist();
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

    private void statlist()
    {
        switch (look.tag) {
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
        switch (look.tag) {
            case ';':
                match(';');
                code.emitLabel(code.newLabel());
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

    public void stat()
    {
        switch (look.tag)
        {
            case '=':
                match(Token.assign.tag);
                int id_addr = st.lookupAddress(((Word) look).lexeme);
                if (id_addr == -1) {
                    id_addr = count;
                    st.insert(((Word) look).lexeme, count++);
                }
                match(Tag.ID);
                expr();
                code.emit(OpCode.istore, id_addr);
                break;
            case Tag.PRINT:
                match(Tag.PRINT);
                match('(');
                exprlist(true, false);
                match(')');
                break;
            case Tag.READ:
                match(Tag.READ);
                match('(');
                if (look.tag == Tag.ID) {
                    id_addr = st.lookupAddress(((Word) look).lexeme);
                    if (id_addr == -1) //Non e' caricata nella symbol table
                    {
                        id_addr = count;
                        st.insert(((Word) look).lexeme, count++);
                    }
                    match(Tag.ID);
                    match(')');
                    code.emit(OpCode.invokestatic, 0);//!= 1 e' la read
                    code.emit(OpCode.istore, id_addr);
                } else // Non rispetta le regole per essere un identificatore
                    error("Error in grammar (stat) after read( with " + look);
                break;
            case Tag.COND:
                match(Tag.COND);
                int wl_false;// = code.newLabel();
                int uscita = code.newLabel(); // = code.newLabel();
                whenlist(code.newLabel(), (wl_false = code.newLabel()), uscita);
                match(Tag.ELSE);
                stat();
                code.emitLabel(uscita);   //Deve essere l'uscita dei GOto
                break;
            case Tag.WHILE:
                match(Tag.WHILE);
                match('(');
                int cond = code.newLabel();
                code.emitLabel(cond); //Mi permette di tornare indietro
                int be_true = code.newLabel();
                int be_false = code.newLabel();
                bexpr(be_true, be_false);
                code.emitLabel(be_true);
                match(')');
                stat();
                code.emit(OpCode.GOto, cond);
                code.emitLabel(be_false);
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

    private void whenlist(int wl_true, int wl_false, int uscita)
    {

        switch (look.tag) {
            case Tag.WHEN:
                whenitem(wl_true, wl_false, uscita);
                whenlistp(code.newLabel(), code.newLabel(), uscita);
                break;
            default:
                error("Errore in whenlist");
        }
    }

    private void whenlistp(int wlp_true, int wlp_false, int uscita)
    {
        switch (look.tag) {
            case Tag.WHEN:
                whenitem(wlp_true, wlp_false, uscita);
                whenlistp(code.newLabel(), code.newLabel(), uscita);
                break;
            case Tag.ELSE:
                break;
            default:
                error("Errore in whenlistp");
        }
    }

    private void whenitem(int wi_true, int wi_false, int uscita)
    {

        switch (look.tag) {
            case Tag.WHEN:
                match(Tag.WHEN);
                match('(');
                //wi_true = code.newLabel();
                bexpr(wi_true, wi_false);
                //code.emit(OpCode.GOto, uscita);
                match(')');
                match(Tag.DO);
                code.emitLabel(wi_true);
                stat();
                code.emit(OpCode.GOto, uscita);
                code.emitLabel(wi_false);
                break;
            default:
                error("Errore in whenlist");
        }
    }

    private void bexpr(int be_true, int be_false)
    {
        switch (look.tag) {
            case Tag.RELOP:
                Word relop = (Word) look; //Mi serve per capire che simbolo RELOP c'e'
                match(Tag.RELOP);
                expr();
                expr();
                //Se e' true continuo il codice, se è false salto sul suo inverso
                switch (relop.lexeme) {
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

    private void expr()
    {
        switch (look.tag)
        {
            case '+':
                match('+');
                match('(');
                exprlist(false, true);
                match(')');
                break;
            case '*':
                match('*');
                match('(');
                exprlist(false, false);
                match(')');
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
                code.emit(OpCode.ldc, ((NumberTok) look).lexenum); //Per prendere il valore numerico di look da caricare per l'assembly
                match(Tag.NUM);
                break;
            case Tag.ID:
                int id_addr = st.lookupAddress(((Word) look).lexeme);
                if (id_addr == -1) {
                    id_addr = count;
                    st.insert(((Word) look).lexeme, count++);
                }
                match(Tag.ID);
                code.emit(OpCode.iload, id_addr);
                break;
            default:
                error("Errore in expr");
        }
    }

    private void exprlist(boolean printB, boolean operatore)
    {
        switch (look.tag) {
            case '+':
            case '*':
            case '-':
            case '/':
            case Tag.NUM:
            case Tag.ID:
                expr();
                if (printB)
                    code.emit(OpCode.invokestatic, 1);
                exprlistp(printB, operatore);
                break;
            default:
                error("Errore in exprlist");
        }
    }

    private void exprlistp(boolean print, boolean operatore)
    {
        switch (look.tag)
        {
            case '+':
            case '*':
            case '-':
            case '/':
            case Tag.NUM:
            case Tag.ID:
                expr();
                if(!print && operatore)
                    code.emit(OpCode.iadd);
                else if(!print && !operatore)
                    code.emit(OpCode.imul);
                else
                    code.emit(OpCode.invokestatic, 1);
                exprlistp(print, operatore);
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
        String path = "testParserProg.lft"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Translator translator = new Translator(lex, br);
            translator.prog();
            System.out.println("Input OK");
            br.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}

