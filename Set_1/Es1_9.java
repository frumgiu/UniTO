package Set_1;

public class Es1_9 //Risolto
{
    public static boolean scan(String s)
    {
        int stato = 0;
        for(int i = 0; i < s.length() && stato >= 0; i++)
        {
            char c = s.charAt(i);
            switch (stato)
            {
                case 0:
                    if (c == '/')
                        stato = 1;
                    else
                        stato = -1;
                    break;
                case 1:
                    if (c == '*')
                        stato = 2;
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
                        stato = 4;
                    else if (c == 'a')
                        stato = 2;
                    else if (c == '*')
                        stato = 3;
                    else
                        stato = -1;
                    break;
                case 4: //Se entra nello stato 4 vuol dire che la stringa non e' finita ma il commento si
                        //ma non posso avere altro dopo '*/' --> non va bene
                    stato = -1;
                    break;
            }
        }
        return stato == 4;
    }

    public static void main(String[] args)
    {
        System.out.println(scan(args[0]) ? "OK" : "NOPE");  //Ok
    }
}