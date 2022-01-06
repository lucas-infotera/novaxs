package br.com.infotera.it.novaxs;

import br.com.infotera.common.util.Utils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
public class NovaxsApplication {


    public static String nrVersao = "";

    public static void main(String[] args) {
        if (args.length > 0) {
            Utils.setTpAmbiente(args[0]);
        } else {
            Utils.setTpAmbiente("H");
        }
        nrVersao = NovaxsApplication.class.getPackage().getImplementationVersion();
        SpringApplication.run(NovaxsApplication.class, args);
    }

    @PostConstruct
    public void init() {
        // Setting Spring Boot SetTimeZone
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

}
