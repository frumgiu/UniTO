public class Es1_8 //Risolto
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
                        stato = 12;
                    break;
                case 2:
                    if (c == 'u')
                        stato = 3;
                    else
                        stato = 16;
                    break;
                case 3:
                    if (c == 'l')
                        stato = 4;
                    else
                        stato = 19;
                    break;
                case 4:
                    if (c == 'i')
                        stato = 5;
                    else
                        stato = 21;
                    break;
                case 5:
                    if (c == 'a')
                        stato = 6;
                    else
                        stato = 22; //L'ultima lettera e' diversa da 'a'
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
                case 12: //La seconda lettera e' diversa da 'i'
                    if (c == 'u')
                        stato = 13;
                    else
                        stato = -1;
                    break;
                case 13:
                    if (c == 'l')
                        stato = 14;
                    else
                        stato = -1;
                    break;
                case 14:
                    if (c == 'i')
                        stato = 15;
                    else
                        stato = -1;
                    break;
                case 15:
                    if (c == 'a')
                        stato = 6;
                    else
                        stato = -1;
                    break;
                case 16: //La terza lettera e' diversa da 'u'
                    if (c == 'l')
                        stato = 17;
                    else
                        stato = -1;
                    break;
                case 17:
                    if (c == 'i')
                        stato = 18;
                    else
                        stato = -1;
                    break;
                case 18:
                    if (c == 'a')
                        stato = 6;
                    else
                        stato = -1;
                    break;
                case 19: //La quarta lettera e' diversa da 'l'
                    if (c == 'i')
                        stato = 20;
                    else
                        stato = -1;
                    break;
                case 20:
                    if (c == 'a')
                        stato = 6;
                    else
                        stato = -1;
                    break;
                case 21: //La quinta lettera e' diversa da 'i'
                    if (c == 'a')
                        stato = 6;
                    else
                        stato = -1;
                    break;
            }
        }
        return stato == 6 || stato == 22;
    }

    public static void main(String[] args)
    {
        System.out.println(scan("Giulia") ? "Ok" : "Nope"); //Ok
        System.out.println(scan("Giul*a") ? "Ok" : "Nope"); //Ok
        System.out.println(scan("*iu*ia") ? "Ok" : "Nope"); //Nope
        System.out.println(scan("Gi*lea") ? "Ok" : "Nope"); //Nope

    }
}