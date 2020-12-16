import java.io.*;

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
        //Commentando le righe riguardanti s_next dimezza il numero di etichette
        switch (look.tag) {
            case '{':
            case '=':
            case Tag.PRINT:
            case Tag.READ:
            case Tag.COND:
            case Tag.WHILE:
                stat(sl_next);
                statlistp(sl_next);
                break;
            default:
                error("Errore in statlist");
        }
    }
    private void statlistp(int slp_next)
    {
        switch (look.tag) {
            case ';':
                match(';');
                code.emitLabel(code.newLabel());
                stat(slp_next);
                statlistp(slp_next);
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
                exprlist(look, true, false);
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
                int wl_false = code.newLabel();
                whenlist(wl_false);
                match(Tag.ELSE);
                stat(wl_false);
                code.emitLabel(wl_false);
                break;
            case Tag.WHILE:
                match(Tag.WHILE);
                match('(');
                int cond = code.newLabel();
                code.emitLabel(cond); //Mi permette di tornare indietro
                int be_false = code.newLabel();
                bexpr(be_false);
                code.emitLabel(be_false);
                System.out.println(be_false);
                match(')');
                stat(be_false);
                code.emit(OpCode.GOto, cond);
                code.emitLabel(be_false);
                break;
            case '{':
                match('{');
                statlist(s_next);
                match('}');
                break;
            default:
                error("Errore in stat");
        }
    }
    private void whenlist(int wl_false)
    {
        switch (look.tag) {
            case Tag.WHEN:
                whenitem(wl_false);
                whenlistp(wl_false);
                break;
            default:
                error("Errore in whenlist");
        }
    }
    private void whenlistp(int wlp_false)
    {
        switch (look.tag) {
            case Tag.WHEN:
                whenitem(wlp_false);
                whenlistp(wlp_false);
                break;
            case Tag.ELSE:
                break;
            default:
                error("Errore in whenlistp");
        }
    }
    private void whenitem(int wi_false)
    {
        int uscita = wi_false;
        switch (look.tag) {
            case Tag.WHEN:
                match(Tag.WHEN);
                match('(');
                wi_false = code.newLabel();
                bexpr(wi_false);
                match(')');
                match(Tag.DO);
                stat(wi_false);
                code.emit(OpCode.GOto, uscita);
                code.emitLabel(wi_false);
                break;
            default:
                error("Errore in whenlist");
        }
    }
    private void bexpr(int be_false)
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
                        code.emit(OpCode.if_icmpne, be_false);
                        break;
                    case "<>":
                        code.emit(OpCode.if_icmpeq, be_false);
                        break;
                    case "<":
                        code.emit(OpCode.if_icmpge, be_false);
                        break;
                    case "<=":
                        code.emit(OpCode.if_icmpgt, be_false);
                        break;
                    case ">":
                        code.emit(OpCode.if_icmple, be_false);
                        break;
                    case ">=":
                        code.emit(OpCode.if_icmplt, be_false);
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
                Token somma = look;
                match('+');
                match('(');
                exprlist(somma, true, true);
                match(')');
                break;
            case '*':
                Token prodotto = look;
                match('*');
                match('(');
                exprlist(prodotto, true, true);
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
    private void exprlist(Token operando,  boolean printB, boolean operatore)
    {
        switch (look.tag) {
            case '+':
            case '*':
            case '-':
            case '/':
            case Tag.NUM:
            case Tag.ID:
                expr();
                exprlistp(operando, printB, operatore);
                break;
            default:
                error("Errore in exprlist");
        }
    }
    private void exprlistp(Token op, boolean print, boolean operatore)
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
                if(op.tag == '+' && operatore)
                    code.emit(OpCode.iadd);
                else if(op.tag == '*' && operatore)
                    code.emit(OpCode.imul);
                exprlistp(op, print, operatore);
                break;
            case ')':
                code.emit(OpCode.invokestatic, 1);
                operatore = false;
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

