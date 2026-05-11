package dtu.softwareHuset;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import dtu.softwareHuset.app.Activity;
import dtu.softwareHuset.app.Company;
import dtu.softwareHuset.app.Project;
import dtu.softwareHuset.app.projectSaveshandler;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class projectSavesSteps {

    Company company;
    Project project;
    Activity activity;

    private static final File CSV = new File("data/projectSaves.csv");
    private byte[] originalContent;

    public projectSavesSteps(Company company) {
        this.company = company;
    }

    @Before("@saves")
    public void backupAndClean() throws IOException {
        originalContent = CSV.exists() ? Files.readAllBytes(CSV.toPath()) : null;
        if (CSV.exists()) CSV.delete();
    }

    @After("@saves")
    public void restore() throws IOException {
        if (CSV.exists()) CSV.delete();
        if (originalContent != null) {
            Files.write(CSV.toPath(), originalContent);
        }
    }

    // Author: GLS0712
    @Given("a project {string} with id {string} is in memory")
    public void a_project_with_id_is_in_memory(String name, String id) {
        company.createProject(name);
        project = company.getProject(name);
        project.setId(id);
    }

    // Author: GLS0712
    @And("the project {string} has an activity named {string}")
    public void the_project_has_an_activity_named(String projectName, String activityName) {
        project = company.getProject(projectName);
        project.createActivity(company.getEmployees().get(0), activityName, "");
        activity = project.getActivityFromName(activityName);
    }

    // Author: GLS0712
    @When("the project {string} is saved to persistent storage")
    public void the_project_is_saved_to_persistent_storage(String name) throws IOException {
        company.writeProjectStub(company.getProject(name));
    }

    // Author: GLS0712
    @When("the activity {string} of project {string} is saved to persistent storage")
    public void the_activity_of_project_is_saved_to_persistent_storage(String activityName, String projectName) throws IOException {
        project = company.getProject(projectName);
        activity = project.getActivityFromName(activityName);
        company.writeActivityStub(project, activity);
    }

    // Author: GLS0712
    @Then("persistent storage contains a project with id {string}")
    public void persistent_storage_contains_a_project_with_id(String id) throws IOException {
        List<List<String>> rows = new projectSaveshandler().load();
        assertTrue(rows.stream().anyMatch(row -> !row.isEmpty() && row.get(0).equals(id)));
    }

    // Author: GLS0712
    @Then("persistent storage contains an activity {string} for project {string}")
    public void persistent_storage_contains_an_activity_for_project(String activityName, String projectId) throws IOException {
        List<List<String>> rows = new projectSaveshandler().load();
        assertTrue(rows.stream().anyMatch(row ->
            row.size() > 4 && row.get(0).equals(projectId) && row.get(4).equals(activityName)
        ));
    }

    // Author: GLS0712
    @Given("the project {string} with id {string} is only in persistent storage")
    public void the_project_with_id_is_only_in_persistent_storage(String name, String id) throws IOException {
        company.createProject(name);
        project = company.getProject(name);
        project.setId(id);
        company.writeProjectStub(project);
        company.getProjects().remove(project);
    }

    // Author: GLS0712
    @Given("the project {string} with id {string} and activity {string} are only in persistent storage")
    public void the_project_with_id_and_activity_are_only_in_persistent_storage(String name, String id, String activityName) throws IOException {
        company.createProject(name);
        project = company.getProject(name);
        project.setId(id);
        project.createActivity(company.getEmployees().get(0), activityName, "");
        activity = project.getActivityFromName(activityName);
        company.writeActivityStub(project, activity);
        company.getProjects().remove(project);
    }

    // Author: GLS0712
    @When("projects are loaded from persistent storage")
    public void projects_are_loaded_from_persistent_storage() throws IOException {
        company.loadProjectsFromLogs();
    }

    // Author: GLS0712
    @Then("the project {string} is available in memory")
    public void the_project_is_available_in_memory(String name) {
        assertNotNull(company.getProject(name));
    }

    // Author: GLS0712
    @Then("the project {string} has the activity {string} in memory")
    public void the_project_has_the_activity_in_memory(String projectName, String activityName) {
        assertNotNull(company.getProject(projectName).getActivityFromName(activityName));
    }

    // Author: GLS0712
    @And("the project {string} is still in memory")
    public void the_project_is_still_in_memory(String name) {
        assertNotNull(company.getProject(name));
    }

    // Author: GLS0712
    @Then("there is exactly one project named {string} in memory")
    public void there_is_exactly_one_project_named_in_memory(String name) {
        long count = company.getProjects().stream()
            .filter(p -> p.getName().equals(name))
            .count();
        assertEquals(1, count);
    }

    // Author: GLS0712
    @Given("a project {string} with id {string} is in memory and in persistent storage")
    public void a_project_with_id_is_in_memory_and_in_persistent_storage(String name, String id) throws IOException {
        company.createProject(name);
        project = company.getProject(name);
        project.setId(id);
        company.writeProjectStub(project);
    }

    // Author: GLS0712
    @Given("a project {string} with id {string} and activity {string} are in memory and in persistent storage")
    public void a_project_with_id_and_activity_are_in_memory_and_in_persistent_storage(String name, String id, String activityName) throws IOException {
        company.createProject(name);
        project = company.getProject(name);
        project.setId(id);
        project.createActivity(company.getEmployees().get(0), activityName, "");
        activity = project.getActivityFromName(activityName);
        company.writeActivityStub(project, activity);
    }

    // Author: GLS0712
    @When("the project {string} is deleted")
    public void the_project_is_deleted(String name) throws IOException {
        company.deleteProject(company.getProject(name));
    }

    // Author: GLS0712
    @When("the activity {string} is deleted from project {string}")
    public void the_activity_is_deleted_from_project(String activityName, String projectName) throws IOException {
        project = company.getProject(projectName);
        activity = project.getActivityFromName(activityName);
        company.deleteActivity(project, activity);
    }

    // Author: GLS0712
    @Then("persistent storage does not contain a project with id {string}")
    public void persistent_storage_does_not_contain_a_project_with_id(String id) throws IOException {
        List<List<String>> rows = new projectSaveshandler().load();
        assertTrue(rows.stream().noneMatch(row -> !row.isEmpty() && row.get(0).equals(id)));
    }

    // Author: GLS0712
    @Then("persistent storage does not contain an activity {string} for project {string}")
    public void persistent_storage_does_not_contain_an_activity_for_project(String activityName, String projectId) throws IOException {
        List<List<String>> rows = new projectSaveshandler().load();
        assertTrue(rows.stream().noneMatch(row ->
            row.size() > 4 && row.get(0).equals(projectId) && row.get(4).equals(activityName)
        ));
    }
}
