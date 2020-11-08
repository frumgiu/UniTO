public class NumberTok extends Token
{
    public int lexenum = 0;

    public NumberTok(int tag, int num) { super(tag); lexenum = num; }
    public String toString() { return "< " + tag + ", " + lexenum + " >"; }
}
