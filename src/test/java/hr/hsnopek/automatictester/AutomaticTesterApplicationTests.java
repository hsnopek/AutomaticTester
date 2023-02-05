package hr.hsnopek.automatictester;

import hr.hsnopek.automatictester.core.model.configuration.Configuration;
import hr.hsnopek.automatictester.core.model.security.SecurityRule;
import hr.hsnopek.automatictester.core.model.step.Step;
import hr.hsnopek.automatictester.core.model.step.actions.impl.ParseJsonAndSetVariableAction;
import hr.hsnopek.automatictester.core.model.step.actions.impl.PrintToConsoleAction;
import hr.hsnopek.automatictester.core.model.step.httpmethods.elements.Body;
import hr.hsnopek.automatictester.core.model.step.httpmethods.elements.Header;
import hr.hsnopek.automatictester.core.model.step.httpmethods.elements.Url;
import hr.hsnopek.automatictester.core.model.step.httpmethods.impl.HttpGet;
import hr.hsnopek.automatictester.core.model.step.httpmethods.impl.HttpPost;
import hr.hsnopek.automatictester.core.parser.Parser;
import hr.hsnopek.automatictester.core.runner.Runner;
import hr.hsnopek.automatictester.util.FileUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ObjectInputFilter;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class AutomaticTesterApplicationTests {

    final
    Runner runner;
    final
    Parser parser;

    Configuration configuration;

    AutomaticTesterApplicationTests() {
        this.parser = new Parser();
        this.runner = new Runner(parser);
    }

    @Test
    void contextLoads() {
        Configuration configuration = new Configuration();
        SecurityRule securityRule = new SecurityRule();
        securityRule.setSslEnabled(false);
        //securityRule.setDomain("localhost");
        //securityRule.setPort("8443");
        //securityRule.setKeyStore("...absolute path to keystore");
        //securityRule.setKeyStorePassword("changeit");
        //securityRule.setTrustStore("absolute path to truststore");
        //securityRule.setTrustStorePassword("changeit");

        configuration.setSecurityRules(List.of(securityRule));

        this.configuration = configuration;
    }

    @Test
    void secondTestExample(){
        hr.hsnopek.automatictester.core.model.Test test = new hr.hsnopek.automatictester.core.model.Test("Test", "This is test case for getting peoples home planets.");
        test.setSteps(new ArrayList<>());

        Step step1 = new Step();
        step1.setName("First step");
        step1.setDescription("Get first 3 people and save homeworld urls.");

        HttpGet firstRequest = new HttpGet(
                "homeworldUrls",
                "global",
                new Url("https://swapi.dev/api/people/${iterator}/?format=json"),
                List.of(new Header("Content-Type", "application/json"))
        );
        firstRequest.setLogHttpResponse(false);
        firstRequest.setIterator("for iterator in [1, 2, 3]");
        firstRequest.setIteratorJsonPathExpression("$.homeworld");

        step1.setHttpMethod(firstRequest);
        step1.setActions(List.of(new PrintToConsoleAction("homeworldUrls")));

        Step step2 = new Step();
        step2.setName("Second step");
        step2.setDescription("Get homeworld names from homeworldUrls.");

        HttpGet secondRequest = new HttpGet(
                "homeworldDetails",
                "global",
                new Url("${homeworldUrl}?format=json"),
                List.of(new Header("Content-Type", "application/json"))
        );
        secondRequest.setLogHttpResponse(false);
        secondRequest.setIterator("for homeworldUrl in {{homeworldUrls}}");
        secondRequest.setIteratorJsonPathExpression("$.name");

        step2.setHttpMethod(secondRequest);
        step2.setActions(List.of(new PrintToConsoleAction("homeworldDetails")));


        test.getSteps().add(step1);
        test.getSteps().add(step2);

        runner.runTest(test);

    }

}
