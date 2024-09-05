package App;

import org.apache.log4j.*;
import org.apache.log4j.xml.DOMConfigurator;
/** 
* Program <code>MyApp</code>
* Klasa <code>MyLogger</code> definiujaca i obslugujaca 
* dziennik zdarzena tzw. log. Wykorzystuje biblioteke log4j a konfiguracja 
* zawarta jest w pliku config/log4j-conf.xml
* @author Kacper Oborzynski	 	
* @version 1.0	01/06/2024
*/
public class MyLogger {

    static final Logger logger = Logger.getLogger("logger");	
    static final Logger myLogger = Logger.getLogger("myLogger");

  /**
	 * Konstruktor bezparametrowy
	 */
    public MyLogger() {
		DOMConfigurator.configure("./config/log4j-conf.xml");
	}

    public static void main(String args[]) throws Exception {

    PatternLayout patternLayout = new PatternLayout("%-5p [%F]: %d %L %m %n");
		
      ConsoleAppender capp = new ConsoleAppender(new SimpleLayout());
      FileAppender fapp = new FileAppender(patternLayout,"fapp.log");
				
      Logger logger = Logger.getLogger("logger");
      logger.addAppender(capp);
      logger.setLevel(Level.INFO);
	
      Logger myLogger = Logger.getLogger("myLogger");
      myLogger.addAppender(fapp);
      myLogger.setLevel(Level.INFO);
	
    }

    public static void writeLog(String level, String message) {
        if(level.equals("INFO")) {
            logger.info(message);
            myLogger.info(message);
        }
	}
}
