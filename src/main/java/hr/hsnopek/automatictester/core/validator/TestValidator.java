package hr.hsnopek.automatictester.core.validator;

import hr.hsnopek.automatictester.core.model.Test;
import hr.hsnopek.automatictester.core.model.step.actions.AbstractAction;
import hr.hsnopek.automatictester.core.model.step.Step;

public class TestValidator {

    public static void validateTest(Test test) {
        if(test.getName() == null){
            throw new RuntimeException("All tests are required to have 'name' parameter declared.");
        }
        test.getSteps().forEach(TestValidator::validateStep);
    }

    private static void validateStep(Step step) {
        if(step.getName() == null){
            throw new RuntimeException("All steps are required to have 'name' parameter declared.");
        }
        if(step.getHttpMethod() != null){
            step.getHttpMethod().validate();
        }
        if(step.getActions() != null){
            step.getActions().forEach(AbstractAction::validate);
        }
    }

}
