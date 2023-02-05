package hr.hsnopek.automatictester.core.model.step.actions;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public abstract class AbstractAction {

    public AbstractAction() {
    }

    public abstract void validate();
}
