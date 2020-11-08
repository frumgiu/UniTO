package Set_1;

public class Es1_6 //Risolto
{
    public static boolean check(String s)
    {
        int stato = 0;
        for (int i = 0; i < s.length() && stato >= 0; i++)
        {
            char c = s.charAt(i);
            switch (stato)
            {
                case 0:
                    if (c == 'a')
                        stato = 3;
                    else if (c == 'b')
                        stato = 1;
                    else
                        stato = -1;
                    break;
                case 1:
                    if (c == 'a')
                        stato = 3;
                    else if (c == 'b')
                        stato = 2;
                    else
                        stato = -1;
                    break;
                case 2:
                    if (c == 'a')
                        stato = 3;
                    else
                        stato = -1;
                    break;
                case 3:
                    if ((c == 'a') || (c == 'b'))
                        stato = 3;
                    else
                        stato = -1;
                    break;
            }
        }
        return stato == 3;
    }
    public static void main(String[] args)
    {
        System.out.println(check(args[0]) ? "OK" : "NOPE");  //Ok
    }
}