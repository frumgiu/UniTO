public class Es1_8 //Risolto e corretto
{
    public static boolean scan (String s)
    {
        int stato = 0;
        for(int i = 0; i < s.length() && stato >= 0; i++)
        {
            char c = s.charAt(i);
            switch (stato)
            {
                case 0:
                    if (c == 'G')
                        stato = 1;
                    else
                        stato = 7;
                    break;
                case 1:
                    if (c == 'i')
                        stato = 2;
                    else
                        stato = 8;
                    break;
                case 2:
                    if (c == 'u')
                        stato = 3;
                    else
                        stato = 9;
                    break;
                case 3:
                    if (c == 'l')
                        stato = 4;
                    else
                        stato = 10;
                    break;
                case 4:
                    if (c == 'i')
                        stato = 5;
                    else
                        stato = 11;
                    break;
                case 5:
                    stato = 6;
                    break;
                case 7: //La prima lettera e' diversa da 'G'
                    if (c == 'i')
                        stato = 8;
                    else
                        stato = -1;
                    break;
                case 8:
                    if (c == 'u')
                        stato = 9;
                    else
                        stato = -1;
                    break;
                case 9:
                    if (c == 'l')
                        stato = 10;
                    else
                        stato = -1;
                    break;
                case 10:
                    if (c == 'i')
                        stato = 11;
                    else
                        stato = -1;
                    break;
                case 11:
                    if (c == 'a')
                        stato = 6;
                    else
                        stato = -1;
                    break;
            }
        }
        return stato == 6;
    }

    public static void main(String[] args)
    {
        System.out.println(scan(args[0]) ? "Ok" : "Nope"); //Ok
    }
}