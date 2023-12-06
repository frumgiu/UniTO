public class NumberTok extends Token
{
    public int lexenum = 0;

    public NumberTok(int t, int num) { super(t); lexenum = num; }
    public String toString() { return "< " + tag + ", " + lexenum + " >"; }
}
