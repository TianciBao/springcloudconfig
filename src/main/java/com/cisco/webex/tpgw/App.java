package com.cisco.webex.tpgw;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.cisco.webex.tpgw.service.LocusService;

@SpringBootApplication
@EnableScheduling
public class App {
    private static Logger logger = Logger.getLogger(App.class);

    public static void main( String[] args )
    {
        String locusDns = getParameterByName(args, "--locus-dns");
        if(locusDns == null) {
            logger.error("Please set locus dns in cmd line, like: java -jar NewLocus.jar --locus-dns=erkmock02.qa.webex.com");
            System.exit(1);
        }

        LocusService.setLocusDns(locusDns);
        SpringApplication.run(App.class, args);
    }
    
    // @PostConstruct
    // public void setLocusDnsByParameter(ApplicationArguments args) {
    //     String locusDns = getParameterByName(args.getSourceArgs(), "--locus-dns");
    //     if(locusDns != null)
    //         LocusService.setLocusDns(locusDns);
    // }

    private static String getParameterByName(String[] args,String name) {
		for(String arg : args) {
			if(arg.startsWith(name + "=")) {
				String[] tmp = arg.split("=");
				if(tmp.length == 2)
					return tmp[1];
			}
		}
		return null;
	}
}
