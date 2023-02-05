package hr.hsnopek.automatictester.core.model;

import hr.hsnopek.automatictester.core.model.configuration.Configuration;
import hr.hsnopek.automatictester.core.model.step.Step;

import javax.xml.bind.annotation.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@XmlRootElement(name = "Test")
@XmlAccessorType(XmlAccessType.FIELD)
public class Test {

    @XmlAttribute(name="name")
    private String name;
    @XmlAttribute(name="description")
    private String description;
    @XmlElementWrapper(name="Steps")
    @XmlElement(name="Step")
    private List<Step> steps;

    @XmlElement(name="Configuration")
    private Configuration configuration;

    @XmlTransient
    private Map<String, String> globalVariables = new LinkedHashMap<>();
    @XmlTransient
    private Map<String, List<String>> globalIterationResults = new LinkedHashMap<>();

    public Test() {
    }

    public Test(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Test(String name, String description, List<Step> steps, Configuration configuration) {
        this.name = name;
        this.description = description;
        this.steps = steps;
        this.configuration = configuration;
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

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public Map<String, String> getGlobalVariables() {
        return globalVariables;
    }

    public Map<String, List<String>> getGlobalIterationResults() {
        return globalIterationResults;
    }

    public void setGlobalIterationResults(Map<String, List<String>> globalIterationResults) {
        this.globalIterationResults = globalIterationResults;
    }
}
