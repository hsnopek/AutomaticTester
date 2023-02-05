package hr.hsnopek.automatictester.core.model.step.actions.impl;

import hr.hsnopek.automatictester.core.model.step.actions.DatasetAction;
import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class ParseJsonAndSetVariableAction extends DatasetAction {

    public ParseJsonAndSetVariableAction(){}

    @Override
    public void validate() {
        // general action rules
        if(StringUtils.isBlank(getScope())){
            // TODO: this is stupid.. some actions will not have need for scope field, but I will let this be for now
            throw new RuntimeException("All actions are required to have 'scope' property declared.");
        } else {
            if(!StringUtils.equalsIgnoreCase("global", getScope())
                    && !StringUtils.equalsIgnoreCase("local", getScope())
            ){
                throw new RuntimeException("Invalid value for 'scope' property. Allowed values are 'global' and 'local'.");
            }
        }
        if(StringUtils.isBlank(getVariableName())){
            throw new RuntimeException("Invalid value for 'variableName' property. Element is required and must not be empty.");
        }
        if(StringUtils.isBlank(getDatasetName())){
            throw new RuntimeException("Invalid value for 'datasetName' property. Element is required and must not be empty.");
        }
        if(StringUtils.isBlank(getJsonPathExpression())){
            throw new RuntimeException("Invalid value for 'jsonPathExpression' property. Element is required and must not be empty.");
        }
    }

    public ParseJsonAndSetVariableAction(String scope, String datasetName, String jsonPathExpression, String variableName){
        super(scope, datasetName, jsonPathExpression, variableName);
    }

}
