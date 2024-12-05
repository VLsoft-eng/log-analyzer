package backend.academy;

import backend.academy.jcommander.Args;
import com.beust.jcommander.JCommander;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Main {
    public static void main(String[] args) {
        Args params = new Args();

        try {
            JCommander.newBuilder()
                .addObject(params)
                .build()
                .parse(args);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return;
        }

        LogAnalyzerFacade logAnalyzerFacade = new LogAnalyzerFacade(System.out);
        logAnalyzerFacade.analyze(params);
    }
}
