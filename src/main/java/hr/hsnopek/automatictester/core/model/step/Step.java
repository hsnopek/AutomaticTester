package hr.hsnopek.automatictester.core.model.step;

import hr.hsnopek.automatictester.core.model.step.httpmethods.AbstractHttpMethod;
import hr.hsnopek.automatictester.core.model.step.httpmethods.impl.HttpPost;
import hr.hsnopek.automatictester.core.model.step.actions.AbstractAction;
import hr.hsnopek.automatictester.core.model.step.actions.impl.ParseJsonAndSetVariableAction;
import hr.hsnopek.automatictester.core.model.step.actions.impl.PrintToConsoleAction;
import hr.hsnopek.automatictester.core.model.step.httpmethods.impl.HttpGet;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@XmlAccessorType(XmlAccessType.FIELD)
public class Step {

    @XmlAttribute
    private String name;
    @XmlAttribute
    private String description;

    @XmlElements({
            @XmlElement(name="HttpGet",type=HttpGet.class),
            @XmlElement(name="HttpPost",type= HttpPost.class),
    })
    private AbstractHttpMethod httpMethod;

    @XmlElementWrapper(name="Actions")
    @XmlElements({
            @XmlElement(name="ParseJsonAndSetVariable",type=ParseJsonAndSetVariableAction.class),
            @XmlElement(name="PrintToConsole",type=PrintToConsoleAction.class),
    })
    private List<AbstractAction> actions = new ArrayList<>();


    @XmlTransient
    Map<String, String> localVariables = new LinkedHashMap<>();

    public Step() {
    }

    public Step(String name, String description, AbstractHttpMethod httpMethod) {
        this.name = name;
        this.description = description;
        this.httpMethod = httpMethod;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AbstractHttpMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(AbstractHttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    public List<AbstractAction> getActions() {
        return actions;
    }

    public void setActions(List<AbstractAction> actions) {
        this.actions = actions;
    }


    public Map<String, String> getLocalVariables() {
        return localVariables;
    }

    public void setLocalVariables(Map<String, String> localVariables) {
        this.localVariables = localVariables;
    }


}
