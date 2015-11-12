package net.masterthought.cucumber.json.support;

import java.util.ArrayList;
import java.util.List;

import net.masterthought.cucumber.json.Step;
import net.masterthought.cucumber.util.Util;

public class TagObject {

    private final String tagName;
    private final List<ScenarioTag> scenarios = new ArrayList<>();

    private final String fileName;
    private int scenarioCounter;
    private StatusCounter scenariosStatusCounter = new StatusCounter();
    private StatusCounter stepsStatusCounter = new StatusCounter();
    private long totalDuration;
    private int totalSteps;

    /** Status for current tag: {@link Status#PASSED} if all scenarios pass {@link Status#FAILED} otherwise. */
    private Status status = Status.PASSED;

    public TagObject(String tagName) {
        this.tagName = tagName;

        // eliminate characters that might be invalid as a file name
        this.fileName = tagName.replace("@", "").replaceAll(":", "-").trim() + ".html";
    }

    public String getTagName() {
        return tagName;
    }

    public String getFileName() {
        return fileName;
    }

    public List<ScenarioTag> getScenarios() {
        return scenarios;
    }

    public void addScenarios(ScenarioTag scenarioTag) {
        scenarios.add(scenarioTag);

        if (status != Status.FAILED && scenarioTag.getScenario().getStatus() != Status.PASSED) {
            status = Status.FAILED;
        }

        if (scenarioTag.getScenario().isScenario()) {
            scenarioCounter++;
            scenariosStatusCounter.incrementFor(scenarioTag.getScenario().getStatus());
        }

        for (Step step : scenarioTag.getScenario().getSteps()) {
            stepsStatusCounter.incrementFor(step.getStatus());
            totalDuration += step.getDuration();
            totalSteps++;
        }
    }

    public int getNumberOfScenarios() {
        return scenarioCounter;
    }

    public Integer getNumberOfPassingScenarios() {
        return scenariosStatusCounter.getValueFor(Status.PASSED);
    }

    public Integer getNumberOfFailingScenarios() {
        return scenariosStatusCounter.getValueFor(Status.FAILED);
    }

    public String getTotalDuration() {
        return Util.formatDuration(totalDuration);
    }

    public int getNumberOfSteps() {
        return totalSteps;
    }

    public int getNumberOfStatus(Status status) {
        return stepsStatusCounter.getValueFor(status);
    }

    /** No-parameters method required for velocity template. */
    public int getNumberOfPasses() {
        return getNumberOfStatus(Status.PASSED);
    }

    /** No-parameters method required for velocity template. */
    public int getNumberOfFailures() {
        return getNumberOfStatus(Status.FAILED);
    }

    /** No-parameters method required for velocity template. */
    public int getNumberOfSkipped() {
        return getNumberOfStatus(Status.SKIPPED);
    }

    /** No-parameters method required for velocity template. */
    public int getNumberOfUndefined() {
        return getNumberOfStatus(Status.UNDEFINED);
    }

    /** No-parameters method required for velocity template. */
    public int getNumberOfMissing() {
        return getNumberOfStatus(Status.MISSING);
    }

    /** No-parameters method required for velocity template. */
    public int getNumberOfPending() {
        return getNumberOfStatus(Status.UNDEFINED);
    }

    public Status getStatus() {
        return status;
    }

    public String getRawStatus() {
        return status.name().toLowerCase();
    }
}