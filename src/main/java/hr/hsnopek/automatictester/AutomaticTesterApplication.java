package hr.hsnopek.automatictester;

import hr.hsnopek.automatictester.core.model.Test;
import hr.hsnopek.automatictester.core.parser.Parser;
import hr.hsnopek.automatictester.core.runner.Runner;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileInputStream;
import java.util.Arrays;

@SpringBootApplication
@Slf4j
public class AutomaticTesterApplication implements ApplicationRunner {

    final
    Runner runner;

    final
    Parser parser;

    @Value("${testPath}")
    private String testPath;

    public AutomaticTesterApplication(Runner runner, Parser parser) {
        this.runner = runner;
        this.parser = parser;
    }

    public static void main(String[] args) {SpringApplication.run(AutomaticTesterApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Application started with command-line arguments: {}", Arrays.toString(args.getSourceArgs()));

        if(StringUtils.isNotBlank(testPath)){
            try(FileInputStream fis = new FileInputStream(testPath)){
                Test test = parser.parseXmlFile(fis);
                runner.runTest(test);
            } catch(Exception e){
                throw e;
            }
        } else {
            log.error("Please run app with: '--testPath=absolute-path-to-your-xml-file.xml' program argument");
            log.error("Shutting down...");
            //System.exit(0);
        }


    }
}
