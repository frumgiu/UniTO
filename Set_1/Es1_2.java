package Set_1;

public class Es1_2
{
    public static boolean check(String s)
    {
        int state = 0;
        for(int i = 0; i < s.length() && state >= 0; i++) //Devo escludere i simboli speciali
        {
            char valore = s.charAt(i);
            switch (state)
            {
                case 0:
                    if ((valore == '_'))
                        state = 1;
                    else if ((valore >= 'a' && valore <= 'z') || (valore >= 'A' && valore <= 'Z'))
                        state = 2;
                    else
                        state = -1;
                    break;
                case 1:
                    if (valore == '_')
                        state = 1;
                    else if ((valore >= '0' && valore <= '9') || (valore >= 'a' && valore <= 'z')|| (valore >= 'A' && valore <= 'Z'))
                        state = 2;
                    else
                        state = -1;
                    break;
                case 2:
                    if ((valore == '_') || (valore >= '0' && valore <= '9') || (valore >= 'a' && valore <= 'z')|| (valore >= 'A' && valore <= 'Z'))
                        state = 2;
                    else
                        state = -1;
                    break;
            }
        }
         return state == 2;
    }
    public static void main(String[] args)
    {
        System.out.println(check(args[0]) ? "OK" : "NOPE");  //Ok

    }
}