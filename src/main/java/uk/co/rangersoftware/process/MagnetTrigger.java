package uk.co.rangersoftware.process;

import uk.co.rangersoftware.config.GlobalConfig;
import uk.co.rangersoftware.downloader.Command;

public class MagnetTrigger {
    private static GlobalConfig config;

    public MagnetTrigger(GlobalConfig config) {
        this.config = config;
    }

    public  void run(String magnetLink){
        try{
            Command command = new Command(config.getMagnetApp(), magnetLink);
            command.run();
        }catch (Exception ex){

        }
    }
}
