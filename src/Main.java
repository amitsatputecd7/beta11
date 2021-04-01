import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
//        String a="'M2MWK-GetCurrentDate-$properties.$timezone'T'M2MWK-GetCurrentTime-$properties.$timezone'";
////        String a="2021-03-22T'M2MWK-GetCurrentTime-$properties.$timezone'";
////        String a="'M2MWK-GetCurrentDate-$properties.$timezone'T15:00:00";
//        String b=a.split("T",2)[0].replaceAll("\'","");
//        String c=a.split("T",2)[1].replaceAll("\'","");
//
//        System.out.println(b);
//        System.out.println(c);


        String a="{M2MWK-GetCurrentDate-$properties.$timezone}T{M2MWK-GetCurrentTime-$properties.$timezone}{M2MWK-GetABC-$properties.$timezone}";
        String example = "United Arab Emirates Dirham (AED)[asdasd] {12asdasd}{22asdasd}{32asdasd}{42asdasd}";
        Matcher m = Pattern.compile("\\{([^}]+)").matcher(a);
//        Matcher m1 = Pattern.compile("\\{([^}]+)\\}").matcher(example);
        ArrayList<String> dum=new ArrayList<>();
        while(m.find()) {
//            System.out.println(m.group(1));
            dum.add(m.group(1));
        }
        System.out.println(dum);
    }
}