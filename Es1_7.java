public class Es1_7 {
    public static boolean check(String s)
    {
        int stato = 0;

        return false;
    }

    public static void main(String[] args) {
        System.out.println(check("bbab") ? "OK" : "NOPE");  //Ok
        System.out.println(check("babbbb") ? "OK" : "NOPE");  //Nope
        System.out.println(check("aa") ? "OK" : "NOPE");  //Ok
        System.out.println(check("ababc") ? "OK" : "NOPE");  //Nope
    }
}