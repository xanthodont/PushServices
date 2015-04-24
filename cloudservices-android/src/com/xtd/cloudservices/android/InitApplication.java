package com.xtd.cloudservices.android;

import java.io.File;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;




import android.app.Application;
import android.os.Environment;
import de.mindpipe.android.logging.log4j.LogConfigurator;

public class InitApplication extends Application {
    @Override
    public void onCreate() {
            super.onCreate();
            LogConfigurator logConfigurator = new LogConfigurator();
            logConfigurator.setFileName(Environment.getExternalStorageDirectory()
                            + File.separator + "cloudservices" + File.separator + "logs"
                            + File.separator + "log4j.txt");
            logConfigurator.setRootLevel(Level.DEBUG);
            logConfigurator.setLevel("org.apache", Level.ERROR);
            logConfigurator.setFilePattern("%d %-5p [%c{2}]-[%L] %m%n");
            logConfigurator.setMaxFileSize(1024 * 1024 * 5);
            logConfigurator.setImmediateFlush(true);
            logConfigurator.configure();
            Logger log = Logger.getLogger(InitApplication.class);
            log.info("My Application Created");
    }

}
