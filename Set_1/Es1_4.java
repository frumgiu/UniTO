package Set_1;

public class Es1_4
{
    //Metodo che torna true se un elemento e' pari, false se e' dispari
    public static int pari(char c)
    {
        if (c >= '0' && c <= '9')
            return (c%2 == 0)? 0 : 1;
        else
            return -1;
    }
    public static boolean turnoBis(String s) //Deve accettare i cognomi composti
    {
        int stato = 0;
        for (int i = 0; i < s.length() && stato >= 0; i++) //Devo scorrere fino all'ultimo elemento della stringa per riconoscerla
        {
            char elem = s.charAt(i); //Prendo l'elem i-esimo della stringa
            switch (stato) {
                case 0:
                    if (pari(elem) == 1) //Elemento e' un numero dispari
                        stato = 1;
                    else if (pari(elem) == 0) //Elemento e' un numero pari
                        stato = 2;
                    else if (elem == ' ') //Se inizia con uno spazio resto in stato 0
                        stato = 0;
                    else
                        stato = -1;
                    break;
                case 1: //Matricola dispari
                    if (pari(elem) == 1) //Elemento e' un numero dispari
                        stato = 1;
                    else if (pari(elem) == 0) //Elemento e' un numero pari
                        stato = 2;
                    else if (elem >= 'L' && elem <= 'Z') //Cognome turno 2
                        stato = 3;
                    else if (elem == 32)
                        stato = 7;
                    else
                        stato = -1;
                    break;
                case 2: //Matricola pari
                    if (pari(elem) == 1) //Elemento e' un numero dispari
                        stato = 1;
                    else if (pari(elem) == 0) //Elemento e' un numero pari
                        stato = 2;
                    else if (elem >= 'A' && elem <= 'K') //Congome turno 3
                        stato = 4;
                    else if (elem == 32) //Lo spazio e' accettato tra matricola e cognome o in un cognome composto
                        stato = 8;
                    else
                        stato = -1;
                    break;
                case 3: //Controlla il cognome
                    if ((elem >= 'a' && elem <= 'z'))
                        stato = 3;
                    else if (elem == 32)
                        stato = 5;
                    else
                        stato = -1;
                    break;
                case 4: //Controlla il cognome
                    if ((elem >= 'a' && elem <= 'z'))
                        stato = 4;
                    else if (elem == 32)
                        stato = 6;
                    else
                        stato = -1;
                    break;
                case 5: //Caso per congome composto
                    if (elem >= 'A' && elem <= 'Z')
                        stato = 3;
                    else if (elem == 32)
                        stato = 5;
                    else
                        stato = -1;
                    break;
                case 6: //Caso per cognome composto
                    if (elem >= 'A' && elem <= 'Z')
                        stato = 4;
                    else if (elem == 32)
                        stato = 6;
                    else
                        stato = -1;
                    break;
                case 7: //Cognome inizia dopo n spazi
                    if (elem >= 'L' && elem <= 'Z')
                        stato = 3;
                    else if (elem == 32)
                        stato = 7;
                    else
                        stato = -1;
                    break;
                case 8: //Cognome inizia dopo n spazi
                    if (elem >= 'A' && elem <= 'K')
                        stato = 4;
                    else if (elem == 32)
                        stato = 8;
                    else
                        stato = -1;
                    break;
            }
        }
        return stato == 3 || stato == 4 || stato == 5 || stato == 6;
    }

    public static void main(String[] args) {
        System.out.println(turnoBis(args[0]) ? "OK" : "NOPE");  //Ok

    }
}