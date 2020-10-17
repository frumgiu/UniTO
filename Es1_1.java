public class Es1_1
{
    //Per fare il complementaredi un diagramma basta complementare gli stati
    public static boolean scan_opposta(String s)
    {
        int stato = 0;
        for (int i = 0; i < s.length() && stato >= 0; i++)
        {
            final char ch = s.charAt(i);

            switch (stato) {
                case 0:
                    if (ch == '0')
                        stato = 1;
                    else if (ch == '1')
                        stato = 0;
                    else
                        stato = -1;
                    break;

                case 1:
                    if (ch == '0')
                        stato = 2;
                    else if (ch == '1')
                        stato = 0;
                    else
                        stato = -1;
                    break;

                case 2:
                    if (ch == '0')
                        stato = 3;
                    else if (ch == '1')
                        stato = 0;
                    else
                        stato = -1;
                    break;

                case 3:
                    if (ch == '0' || ch == '1')
                        stato = 3;
                    else
                        stato = -1;
                    break;
            }
        }
        return stato == 2 || stato == 1 || stato == 0;
    }

    public static void main(String[] args)
    {
        System.out.println(scan_opposta("101") ? "OK" : "NOPE");    //Ok
        System.out.println(scan_opposta("1000101") ? "OK" : "NOPE");//Nope
    }
}