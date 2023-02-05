package hr.hsnopek.automatictester.core.parser;

import hr.hsnopek.automatictester.core.validator.TestValidator;
import hr.hsnopek.automatictester.core.model.Test;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class Parser {

    public Test parseXmlFile(InputStream is) throws JAXBException {
        Test test;
        try {
            log.info("Parsing xml file...");
            test = parseInputStream(is);
            log.info("File successfully parsed!");
            TestValidator.validateTest(test);
        } catch (Exception e){
            log.error("There was an error when parsing xml file.", e);
            throw e;
        }
        return test;
    }
    private Test parseInputStream(InputStream xmlFile) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Test.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        InputStreamReader reader = new InputStreamReader(xmlFile);
        return (Test) unmarshaller.unmarshal(reader);
    }

    public List<String> getVariableNamesFromTemplateLiterals(String str){
        if(StringUtils.isNotBlank(str)) {
            List<String> allMatches = new ArrayList<>();

            Pattern pattern = Pattern.compile("(?<=\\$\\{)(.*?)(?=\\})", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(str);

            while (matcher.find()) {
                allMatches.add(matcher.group());
            }
            return allMatches;
        }
        return new ArrayList<>();
    }
}
