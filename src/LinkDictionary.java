
import com.company.Main;

import java.io.FileNotFoundException;

public class LinkDictionary {
    public static void main (String[] args) {
        try {
            Main app = new Main(args);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}