package Set_1;

public class Es1_3
{
    public static boolean turno(String s)
    {
        int stato = 0;
        for(int i = 0; i < s.length() && stato >= 0; i++) //Devo scorrere fino all'ultimo elemento della stringa per riconoscerla
        {
            char elem = s.charAt(i); //Prendo l'elem i-esimo della stringa
            switch(stato)
            {
                case 0:
                    if (elem == '1' || elem == '3' || elem == '5' || elem == '7' || elem == '9') //Elemento e' un numero dispari
                        stato = 1;
                    else if (elem == '0' || elem == '2' || elem == '4' || elem == '6' || elem == '8') //Elemento e' un numero pari
                        stato = 2;
                    else
                        stato = -1;
                    break;
                case 1:
                    if (elem == '1' || elem == '3' || elem == '5' || elem == '7' || elem == '9') //Elemento e' un numero dispari
                        stato = 1;
                    else if (elem == '0' || elem == '2' || elem == '4' || elem == '6' || elem == '8') //Elemento e' un numero pari
                        stato = 2;
                    else if (elem >= 'A' && elem <= 'K') //Cognome turno 2
                        stato = 3;
                    else
                        stato = -1;
                    break;
                case 2:
                    if (elem == '1' || elem == '3' || elem == '5' || elem == '7' || elem == '9') //Elemento e' un numero dispari
                        stato = 1;
                    else if (elem == '0' || elem == '2' || elem == '4' || elem == '6' || elem == '8') //Elemento e' un numero pari
                        stato = 2;
                    else if (elem >= 'L' && elem <= 'Z') //Congome turno 3
                        stato = 4;
                    else
                        stato = -1;
                    break;
                case 3:
                    if (elem >= 'a' && elem <= 'z')
                        stato = 3;
                    else
                        stato = -1;
                    break;
                case 4:
                    if (elem >= 'a' && elem <= 'z')
                        stato = 4;
                    else
                        stato = -1;
                    break;
            }
        }
        return stato == 3 || stato == 4;
    }
    public static void main (String[] args)
    {
        System.out.println(turno(args[0]) ? "OK" : "NOPE");  //Ok
    }
}
