package hr.hsnopek.automatictester.core.model.step.actions;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public abstract class DatasetAction extends AbstractAction {


    @XmlAttribute
    private String scope;
    @XmlAttribute
    private String datasetName;
    @XmlAttribute
    private String jsonPathExpression;
    @XmlAttribute
    private String variableName;

    public DatasetAction(){}

    public DatasetAction(String scope, String datasetName, String jsonPathExpression, String variableName) {
        this.scope = scope;
        this.datasetName = datasetName;
        this.jsonPathExpression = jsonPathExpression;
        this.variableName = variableName;
    }

    public String getScope() {
        return scope;
    }
    public String getDatasetName() {
        return datasetName;
    }

    public String getJsonPathExpression() {
        return jsonPathExpression;
    }

    public String getVariableName() {
        return variableName;
    }
}
