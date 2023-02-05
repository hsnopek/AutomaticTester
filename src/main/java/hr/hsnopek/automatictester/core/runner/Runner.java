package hr.hsnopek.automatictester.core.runner;

import com.jayway.jsonpath.JsonPath;
import hr.hsnopek.automatictester.core.model.Test;
import hr.hsnopek.automatictester.core.model.step.actions.AbstractAction;
import hr.hsnopek.automatictester.core.model.step.actions.impl.ParseJsonAndSetVariableAction;
import hr.hsnopek.automatictester.core.model.step.actions.impl.PrintToConsoleAction;
import hr.hsnopek.automatictester.core.model.configuration.Configuration;
import hr.hsnopek.automatictester.core.model.step.httpmethods.elements.Body;
import hr.hsnopek.automatictester.core.model.step.httpmethods.elements.Header;
import hr.hsnopek.automatictester.core.model.step.Step;
import hr.hsnopek.automatictester.core.model.step.httpmethods.elements.Url;
import hr.hsnopek.automatictester.core.model.step.httpmethods.AbstractHttpMethod;
import hr.hsnopek.automatictester.core.model.step.httpmethods.impl.HttpGet;
import hr.hsnopek.automatictester.core.model.step.httpmethods.impl.HttpPost;
import hr.hsnopek.automatictester.core.parser.Parser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class Runner {

    private final Parser parser;

    public void runTest(Test test) {
        log.info("Running test...");
        log.info("***** {}, description: '{}' ***** {}", test.getName(), test.getDescription(), System.lineSeparator());
        for (Step step : test.getSteps()) {
            log.info("***** {}, description: {} ***** {}", step.getName(), step.getDescription(), System.lineSeparator());
            boolean iteratorDetected = StringUtils.isNotBlank(step.getHttpMethod().getIterator());
            if(iteratorDetected){
                log.info("***** Iterator detected: '{}' ***** {}", step.getHttpMethod().getIterator(), System.lineSeparator());
                runIterator(test, step);
            } else {
                executeStep(test, step);
            }
        }
    }

    private void runIterator(Test test, Step step) {
        // remove consecutive whitespaces from users iterator expression
        String sanitizedIteratorExpression = sanitizeIteratorExpression(step);
        String[] tokenizedExpression = sanitizedIteratorExpression.split(" ");
        // if first word in sentence is FOR token, run forExpressionIterator, else continue
        runForExpressionIterator(test, step, sanitizedIteratorExpression, tokenizedExpression, tokenizedExpression);
    }

    private void runForExpressionIterator(Test test, Step step, String sanitizedIteratorExpression, String[] splittedExpression, String[] tokenizedExpression) {

        // get first word in expression to recognize which iterator must be executed
        String firstWord = tokenizedExpression[0];

        List<String> tokenList = Arrays.asList("FOR", "IN");
        List<String> arrayItems = new ArrayList<>();

        Pattern allBetweenTheBracketsPattern = Pattern.compile("\\[(.*?)]", Pattern.CASE_INSENSITIVE);
        Pattern allBetweenTheBracketsListPattern = Pattern.compile("\\{\\{(.*?)}}", Pattern.CASE_INSENSITIVE);
        Matcher allBetweenBracketsMatcher = allBetweenTheBracketsPattern.matcher(sanitizedIteratorExpression);
        Matcher allBetweenBracketsListMatcher = allBetweenTheBracketsListPattern.matcher(sanitizedIteratorExpression);

        if(tokenList.contains(firstWord.toUpperCase()) & "FOR".equalsIgnoreCase(firstWord)){
            // Next token should be variable declaration
            String iteratorVariableName = splittedExpression[1];
            // Next token should be IN
            String inToken = splittedExpression[2];

            // Check if it is indeed IN token in expression
            if(tokenList.contains(inToken.toUpperCase())){
                // check if array is defined
                boolean arrayDefined = false;
                while (allBetweenBracketsMatcher.find()) {
                    String match = allBetweenBracketsMatcher.group(1);
                    arrayItems = Arrays.stream(match.split(",")).map(String::strip).collect(Collectors.toList());
                    arrayDefined = true;
                }
                while (allBetweenBracketsListMatcher.find()) {
                    String match = allBetweenBracketsListMatcher.group(1);
                    arrayItems = test.getGlobalIterationResults().get(match);
                    arrayDefined = true;
                }
                if (arrayDefined){
                    // use array items as local variables for current step iteration
                    arrayItems.forEach( (item) -> {
                        step.getLocalVariables().put(iteratorVariableName, item);
                        executeHttpMethod(test, step);
                    });
                }
            }
        }
        executeActions(test, step);
    }

    private String sanitizeIteratorExpression(Step step) {
        return step.getHttpMethod().getIterator().replaceAll("^ +| +$|( )+", "$1");
    }

    private void executeStep(Test test, Step step) {
        if (step.getHttpMethod() != null) {
            executeHttpMethod(test, step);
        }
        executeActions(test, step);
    }

    private void executeActions(Test test, Step step) {
        log.info("*** Running actions ***");
        for (AbstractAction action : step.getActions()) {
            if (action instanceof PrintToConsoleAction) {
                log.info("*** PrintToConsoleAction ***");
                PrintToConsoleAction printToConsoleAction = ((PrintToConsoleAction) action);
                runPrintToConsoleAction(test, step, printToConsoleAction);
            } else if (action instanceof ParseJsonAndSetVariableAction) {
                log.info("*** ParseJsonAndSetVariableAction ***");
                ParseJsonAndSetVariableAction parseJsonAndSetVariableAction = ((ParseJsonAndSetVariableAction) action);
                runParseJsonAndSetVariableAction(test, step, parseJsonAndSetVariableAction);
            }
        }
    }

    private void runParseJsonAndSetVariableAction(Test test, Step step, ParseJsonAndSetVariableAction parseJsonAndSetVariableAction) {
        String datasetName = parseJsonAndSetVariableAction.getDatasetName();
        String jsonPathExpression = parseJsonAndSetVariableAction.getJsonPathExpression();
        String variableName = parseJsonAndSetVariableAction.getVariableName();
        String scope = parseJsonAndSetVariableAction.getScope();

        log.info(
                "Parsing json response from dataset with name '{}', with jsonPath '{}' and setting results to {} variable '{}'.",
                datasetName,
                jsonPathExpression,
                parseJsonAndSetVariableAction.getScope(),
                variableName);

        String json = test.getGlobalVariables().get(datasetName);
        Object o = JsonPath.read(json, jsonPathExpression);
        String extractedValue = String.valueOf(o);

        if("global".equalsIgnoreCase(scope)) {
            test.getGlobalVariables().put(variableName, extractedValue);
        } else {
            step.getLocalVariables().put(variableName, extractedValue);
        }
        log.info("************************************* {}", System.lineSeparator());
    }

    private void runPrintToConsoleAction(Test test, Step step, PrintToConsoleAction printToConsoleAction) {
        String variableName = printToConsoleAction.getVariableName();

        if(test.getGlobalIterationResults() != null && test.getGlobalIterationResults().get(variableName) != null){
            int iterationNumber = 1;
            for(String value : test.getGlobalIterationResults().get(variableName)){
                log.info("Logging value for variable '{}', iteration {}", variableName, iterationNumber);
                log.info("'{}' = {}", variableName, value);
                log.info("**************************** {}", System.lineSeparator());
                iterationNumber++;
            }
        } else {
            log.info("Logging value for variable '{}'", variableName);
            // try to get local variable first, and if it doesn't exist - get global variable
            String variableValue = StringUtils.isNotBlank(step.getLocalVariables().get(variableName)) ?
                    step.getLocalVariables().get(variableName) : test.getGlobalVariables().get(printToConsoleAction.getVariableName());

            log.info("'{}' = {}", variableName, variableValue);
            log.info("**************************** {}", System.lineSeparator());
        }

    }

    private void executeHttpMethod(Test test, Step step) {
        AbstractHttpMethod httpMethod = step.getHttpMethod();

        replaceTemplateLiterals(test, step, httpMethod);

        ResponseEntity<String> response = executeHttpRequest(test.getConfiguration(), httpMethod);
        httpMethod.setResponseEntity(response);
        httpMethod.logHttpResponse();

        assignResponseBodyToVariable(test, step, httpMethod);
    }



    private void assignResponseBodyToVariable(Test test, Step step, AbstractHttpMethod httpMethod) {
        String datasetName = httpMethod.getDatasetName();
        String resultBody = null;
        if(httpMethod.getResponseEntity() != null){
            resultBody = httpMethod.getResponseEntity().getBody();
        } else {
            System.exit(0);
        }

        log.info("ResponseStatus: {}", httpMethod.getResponseEntity().getStatusCode());

        setGlobalIterationResultList(test, step, datasetName, resultBody);

        if (httpMethod.getScope().equals("global")) {
            log.info("Setting result to global variable '{}'", datasetName);
            test.getGlobalVariables().put(datasetName, resultBody);
        } else if (httpMethod.getScope().equals("local")) {
            log.info("Setting result to local variable '{}'", datasetName);
            step.getLocalVariables().put(datasetName, resultBody);
        }
        log.info("****************** {}", System.lineSeparator());
    }

    private void setGlobalIterationResultList(Test test, Step step, String datasetName, String resultBody) {
        List<String> iterationResultList = test.getGlobalIterationResults().get(datasetName);
        String iteratorJsonPathExpression = step.getHttpMethod().getIteratorJsonPathExpression();

        String value;
        if(StringUtils.isNotBlank(iteratorJsonPathExpression)){
            Object o = JsonPath.read(resultBody, iteratorJsonPathExpression);
            String extractedValue = String.valueOf(o);
            value = StringUtils.isNotBlank(iteratorJsonPathExpression) ? extractedValue: resultBody;
            log.info("Applying jsonPath expression {} to response.", iteratorJsonPathExpression);
            log.info("Parsed value is: '{}'", value);
        } else {
            value = resultBody;
        }

        if(iterationResultList == null || iterationResultList.isEmpty()){
            assert resultBody != null;
            ArrayList<String> initialList = new ArrayList<>();
            initialList.add(value);
            test.getGlobalIterationResults().put(datasetName, initialList);
        } else {
            iterationResultList.add(value);
            test.getGlobalIterationResults().put(datasetName, iterationResultList);
        }
    }

    private ResponseEntity<String> executeHttpRequest(Configuration configuration, AbstractHttpMethod httpMethod) {
        String headers = httpMethod.getHeaders()
                .stream()
                .map( header -> String.format("[key='%s'], [value='%s']", header.getKey(), header.getValue()))
                .collect(Collectors.joining(", "));

        log.info("*** HttpMethod ***");
        if (httpMethod instanceof HttpGet) {
            log.info("Running HttpGet with url {} and headers {}", httpMethod.getUrl().getValue(), headers);
            return ((HttpGet) httpMethod).get(configuration);
        } else if (httpMethod instanceof HttpPost) {
            log.info("Running HttpPost with url {} and headers {}", httpMethod.getUrl().getValue(), headers);
            return ((HttpPost) httpMethod).post(configuration);
        }
        return null;
    }
    private void replaceTemplateLiterals(Test test, Step step, AbstractHttpMethod httpMethod) {

        for(Header header: httpMethod.getHeaders()){
            header.setValue(header.getOriginalValue());
            parser.getVariableNamesFromTemplateLiterals(header.getOriginalValue()).forEach(literal -> {
                String newValue = replaceVariable(test, step, literal);
                if(newValue == null){
                    throw new RuntimeException(String.format("Can't find variable %s",literal));
                }
                String originalValue = header.getOriginalValue();
                String replacedValue = originalValue.replace("${" + literal + "}", newValue);

                header.setValue(replacedValue);
            });
        }

        httpMethod.getUrl().setValue(httpMethod.getUrl().getOriginalValue());
        replaceUrlLiterals(test, step, httpMethod);

        if(httpMethod.getBody() != null) {
            httpMethod.getBody().setValue(httpMethod.getBody().getOriginalValue());
            replaceBodyLiterals(test, step, httpMethod);
        }
    }


    private String replaceVariable(Test test, Step step, String value){
        // rule - if local variable does not exist try to get global variable
        if(StringUtils.isNotBlank(step.getLocalVariables().get(value))){
            return step.getLocalVariables().get(value);
        } else {
            return test.getGlobalVariables().get(value);
        }
    }

    private void replaceBodyLiterals(Test test, Step step, AbstractHttpMethod httpMethod) {
        parser.getVariableNamesFromTemplateLiterals(httpMethod.getBody().getOriginalValue()).forEach(literal -> {
            String newValue = replaceVariable(test, step, literal);
            String originalValue = httpMethod.getBody().getOriginalValue();
            String replacedValue = originalValue.replace("${" + literal + "}", newValue);
            httpMethod.setBody(new Body(originalValue, replacedValue));
        });
    }

    private void replaceUrlLiterals(Test test, Step step, AbstractHttpMethod httpMethod) {
        parser.getVariableNamesFromTemplateLiterals(httpMethod.getUrl().getOriginalValue()).forEach(literal -> {
            String newValue = replaceVariable(test, step, literal);
            String originalValue = httpMethod.getUrl().getOriginalValue();
            String replacedValue = originalValue.replace("${" + literal + "}", newValue);
            httpMethod.setUrl(new Url(originalValue, replacedValue));
        });
    }

}
