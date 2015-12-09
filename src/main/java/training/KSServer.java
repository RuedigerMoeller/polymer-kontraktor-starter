package training;

import org.nustaq.kontraktor.Actor;
import org.nustaq.kontraktor.Callback;
import org.nustaq.kontraktor.Scheduler;
import org.nustaq.kontraktor.annotations.Local;
import org.nustaq.kontraktor.impl.SimpleScheduler;
import org.nustaq.kontraktor.remoting.encoding.SerializerType;
import org.nustaq.kontraktor.remoting.http.Http4K;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by ruedi on 09/12/15.
 */
public class KSServer extends Actor<KSServer> {

    Scheduler clientThreads[];
    KSAppConfig conf;

    @Local
    public void init(KSAppConfig conf) {
        this.conf = conf;
        clientThreads = new Scheduler[conf.sessionThreads];
        for (int i = 0; i < clientThreads.length; i++) {
            clientThreads[i] = new SimpleScheduler(conf.clientQSize,true);
        }
    }

    public static void main(String[] args) throws IOException {
        File root = new File("./web");

        if ( ! new File(root,"index.html").exists() ) {
            System.out.println("Please run with working dir: '[projectroot]");
            System.exit(-1);
        }

        // create server actor + read config
        KSServer app = AsActor(KSServer.class);
        KSAppConfig conf = KSAppConfig.read();
        app.init(conf);

        Http4K.Build(conf.hostName, conf.port)
            .resourcePath(conf.urlRoot)
                .elements( conf.resources )
                .allDev( conf.devMode )
                .build()
            .httpAPI("/api", app)
                .serType(SerializerType.JsonNoRef)
                .setSessionTimeout(30_000)
                .build()
            .build();
    }

}
