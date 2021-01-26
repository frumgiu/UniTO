package Set_1;

public class Es1_10
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
                        stato = 1;
                    else
                        stato = -1;
                    break;
                case 2:
                    if (c == '*')
                        stato = 3;
                    else if (c == 'a' || c == '/')
                        stato = 2;
                    else
                        stato = -1;
                    break;
                case 3:
                    if (c == '/')
                        stato = 0;
                    else if (c == 'a')
                        stato = 2;
                    else if (c == '*')
                        stato = 3;
                    else
                        stato = -1;
                    break;
            }
        }
        return stato == 1 || stato == 0;
    }
    
    public static void main(String[] args)
    {
        System.out.println(scan(args[0]) ? "OK" : "NOPE");  //Ok
    }
}