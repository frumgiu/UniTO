public class Es1_5
{
    public static boolean scan(String s)
    {
        int stato = 0;
        for(int i = 0; i < s.length() && stato >= 0; i++)
        {
            char elem = s.charAt(i);
            switch (stato)
            {
                case 0:
                    if (elem >= 'A' && elem <= 'K')
                        stato = 2;
                    else if (elem >= 'L' && elem <= 'Z')
                        stato = 1;
                    else
                        stato = -1;
                    break;
                case 1: //Cognome L-Z
                    //Caso finale per matricola dispari, se e' pari devo finire nello stato pozzo 3
                    if (elem >= 'a' && elem <= 'z')
                        stato = 1;
                    else if (elem == '0' || elem == '2' || elem == '4' || elem == '6' || elem == '8')
                        stato = 5;
                    else if (elem == '1' || elem == '3' || elem == '5' || elem == '7' || elem == '9')
                        stato = 3;
                    else
                        stato = -1;
                    break;
                case 2: //Cognome A-K
                    if (elem >= 'a' && elem <= 'z')
                        stato = 2;
                    else if (elem == '0' || elem == '2' || elem == '4' || elem == '6' || elem == '8')
                        stato = 4;
                    else if (elem == '1' || elem == '3' || elem == '5' || elem == '7' || elem == '9')
                        stato = 6;
                    else
                        stato = -1;
                    break;
                case 3: //Prima cifra dispari
                    if (elem == '1' || elem == '3' || elem == '5' || elem == '7' || elem == '9')
                        stato = 3;
                    else if (elem == '0' || elem == '2' || elem == '4' || elem == '6' || elem == '8')
                        stato = 5;
                    break;
                case 4: //Prima cifra dispari
                    if (elem == '1' || elem == '3' || elem == '5' || elem == '7' || elem == '9')
                        stato = 6;
                    else if (elem == '0' || elem == '2' || elem == '4' || elem == '6' || elem == '8')
                        stato = 4;
                    break;
                case 5:// Matricola pari non va bene per L-Z
                    if (elem == '1' || elem == '3' || elem == '5' || elem == '7' || elem == '9')
                        stato = 3;
                    else if (elem == '0' || elem == '2' || elem == '4' || elem == '6' || elem == '8')
                        stato = 5;
                    else
                        stato = -1;
                    break;
                case 6:// Matricola dispari non va bene per A-K
                    if (elem == '1' || elem == '3' || elem == '5' || elem == '7' || elem == '9')
                    stato = 6;
                    else if (elem == '0' || elem == '2' || elem == '4' || elem == '6' || elem == '8')
                    stato = 4;
                else
                    stato = -1;
                    break;
            }
        }
        return stato == 3 || stato == 4;
    }
    public static void main(String[] args)
    {
        System.out.println(scan(args[0]) ? "OK" : "NOPE");  //Ok

    }
}