package training;

import org.nustaq.kontraktor.util.Log;
import org.nustaq.kson.Kson;

import java.io.File;

/**
 * Created by ruedi on 09/12/15.
 */
public class KSAppConfig {

    public static KSAppConfig read() {
        try {
            return (KSAppConfig) new Kson().map(KSAppConfig.class).readObject(new File("app.kson"));
        } catch (Exception e) {
            Log.Warn(null, "app.kson not found or parse error. " + e.getClass().getSimpleName() + ":" + e.getMessage());
            try {
                String sampleconf = new Kson().map(KSAppConfig.class).writeObject(new KSAppConfig());
                System.out.println("Defaulting to:\n"+sampleconf);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return new KSAppConfig();
    }

    // initial values are defaults ..

    public String urlRoot = "/";
    public boolean devMode = true;
    public int sessionThreads = 1;
    public int clientQSize = 1000;
    public String hostName = "localhost";
    public int port = 7777;

    public String resources[] = {
         "./web",
         "/home/ruedi/projects/Juptr/src/main/web/bower_components"
    };

}
