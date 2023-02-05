package hr.hsnopek.automatictester.core.model.step.actions.impl;

import hr.hsnopek.automatictester.core.model.step.actions.VoidAction;
import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class PrintToConsoleAction extends VoidAction {

    public PrintToConsoleAction(){
    }

    @Override
    public void validate() {
        if(StringUtils.isBlank(getVariableName())){
            throw new RuntimeException("Invalid value for 'variableName' property. Element is required and must not be empty.");
        }
    }

    public PrintToConsoleAction(String variableName) {
        super(variableName);
    }
}
