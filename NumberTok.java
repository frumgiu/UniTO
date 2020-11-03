public class NumberTok extends Token
{
    public String lexenum = "";

    public NumberTok(int t, String s) { super(t); lexenum = s; }
    public String toString() { return "< " + tag + ", " + lexenum + " >"; }
}
