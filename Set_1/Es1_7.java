package Set_1;

public class Es1_7 //Risolto
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
                    if (c == 'b')
                        stato = 0;
                    else if (c == 'a')
                        stato = 1;
                    else
                        stato = -1;
                    break;
                case 1:
                    if (c == 'a')
                        stato = 1;
                    else if (c == 'b')
                        stato = 2;
                    else
                        stato = -1;
                    break;
                case 2:
                    if (c == 'a')
                        stato = 1;
                    else if (c == 'b')
                        stato = 3;
                    else
                        stato = -1;
                    break;
                case 3:
                    if (c == 'a')
                        stato = 1;
                    else if (c == 'b')
                        stato = 0;
                    else
                        stato = -1;
                    break;
            }
        }
        return stato == 1 || stato == 2 || stato == 3;
    }

    public static void main(String[] args) {
        System.out.println(check(args[0]) ? "OK" : "NOPE");  //Ok

    }
}