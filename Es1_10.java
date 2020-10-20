public class Es1_10 {
    public static boolean scan(String s) {
        int stato = 0;
        for (int i = 0; i < s.length() && stato >= 0; i++) {
            char c = s.charAt(i);
            switch (stato) {
                case 0:
                    if (c == '/') //Inizio nuovo commento
                        stato = 1;
                    else if (c == '*' || c == 'a') //Scritte della stringa
                        stato = 0;
                    else
                        stato = -1;
                    break;
                case 1:
                    if (c == '*') //Inizio nuovo commento
                        stato = 2;
                    else
                        stato = -1;
                    break;
                case 2:
                    if (c == '*') //Inizio fine commento
                        stato = 3;
                    else if (c == 'a') //Corpo del commento
                        stato = 5;
                    else
                        stato = -1;
                    break;
                case 3:
                    if (c == '/') //Fine commento
                        stato = 4;
                    else if (c == 'a') //Altro corpo del commento
                        stato = 5;
                    else if (c == '*')
                        stato = 3;
                    else
                        stato = -1;
                    break;
                case 4://Il commento si e' chiude prima di finire la stringa
                    if (c == '*')
                        stato = 2;
                    else if (c == 'a')
                        stato = 4;
                    else
                        stato = -1;
                    break;
                case 5: //Corpo del commento
                    if (c == 'a' || c == '/')
                        stato = 5;
                    else if (c == '*')
                        stato = 3;
                    else
                        stato = -1;
                    break;
            }
        }
        return stato == 4;
    }

    public static void main(String[] args) {
        System.out.println(scan("/**/") ? "OK" : "NOPE");  //Ok
        System.out.println(scan("/*/") ? "OK" : "NOPE");  //Nope
        System.out.println(scan("/*aa*a*/") ? "OK" : "NOPE");  //Ok
        System.out.println(scan("/**/a*a") ? "OK" : "NOPE");  //Nope
    }
}