public class Es1_10 //Risolto
{
    public static boolean scan(String s)
    {
        int stato = 0;
        for (int i = 0; i < s.length() && stato >= 0; i++)
        {
            char c = s.charAt(i);
            switch (stato)
            {
                case 0:
                    if (c == '/')
                        stato = 1;
                    else if (c == 'a' || c == '*')
                        stato = 0;
                    else
                        stato = -1;
                    break;
                case 1:
                    if (c == '*')
                        stato = 2;
                    else if (c == 'a')
                        stato = 0;
                    else if (c == '/')
                        stato = 4;
                    else
                        stato = -1;
                    break;
                case 2:
                    if (c == '*')
                        stato = 3;
                    else if (c == 'a')
                        stato = 5;
                    else
                        stato = -1;
                    break;
                case 3:
                    if (c == '/')
                        stato = 4;
                    else if (c == 'a')
                        stato = 5;
                    else if (c == '*')
                        stato = 3;
                    else
                        stato = -1;
                    break;
                case 4:
                    if (c == 'a' || c == '/')
                        stato = 0;
                    else if (c == '*')
                        stato = 2;
                    else
                        stato = -1;
                    break;
                case 5:
                    if (c == 'a' || c == '/')
                        stato = 5;
                    else if (c == '*')
                        stato = 3;
                    else
                        stato = -1;
                    break;
            }
        }
        return stato == 4 || stato == 0;
    }

    public static void main(String[] args)
    {
        System.out.println(scan("/**/") ? "OK" : "NOPE");  //Ok
        System.out.println(scan("/*aa*/aa") ? "OK" : "NOPE");  //Ok
        System.out.println(scan("/*/") ? "OK" : "NOPE");  //Nope
        System.out.println(scan("/**/a/*a") ? "OK" : "NOPE");  //Nope
    }
}