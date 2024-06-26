
package acme.features.manager.projectuserstory;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.project.Project;
import acme.entities.projectuserstory.ProjectUserStory;
import acme.entities.userstory.UserStory;
import acme.roles.Manager;

@Service
public class ManagerProjectUserStoryCreateService extends AbstractService<Manager, ProjectUserStory> {

	@Autowired
	private ManagerProjectUserStoryRepository repository;


	@Override
	public void authorise() {
		Manager manager;
		boolean status;

		manager = this.repository.findOneManagerById(super.getRequest().getPrincipal().getActiveRoleId());
		status = super.getRequest().getPrincipal().hasRole(manager);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		ProjectUserStory object = new ProjectUserStory();
		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final ProjectUserStory object) {
		assert object != null;
		super.bind(object, "project", "userStory");

	}

	@Override
	public void validate(final ProjectUserStory object) {
		assert object != null;
		Project project;
		UserStory userStory;
		Manager manager;
		int managerId;

		managerId = super.getRequest().getPrincipal().getActiveRoleId();
		manager = this.repository.findOneManagerById(managerId);
		project = object.getProject();
		userStory = object.getUserStory();

		super.state(project != null, "*", "manager.projectuserstory.form.error.notnull.project");
		super.state(userStory != null, "*", "manager.projectuserstory.form.error.notnull.userstory");

		if (!super.getBuffer().getErrors().hasErrors("project") && !super.getBuffer().getErrors().hasErrors("userStory")) {
			ProjectUserStory existing;

			existing = this.repository.findOneProjectUserStoryByProjectIdAndUserStoryId(project.getId(), userStory.getId());
			super.state(project.getManager().equals(manager) && userStory.getManager().equals(manager), "*", "manager.link.form.error.wrong-manager");
			super.state(existing == null, "*", "manager.projectuserstory.form.error.existing-project-assignation");
			super.state(project.isDraftMode() || !userStory.isDraftMode(), "project", "manager.projectuserstory.form.error.published-project");
		}

	}

	@Override
	public void perform(final ProjectUserStory object) {
		assert object != null;
		this.repository.save(object);
	}

	@Override
	public void unbind(final ProjectUserStory object) {
		assert object != null;

		Collection<UserStory> userStories;
		Collection<Project> projects;
		SelectChoices choicesUS;
		SelectChoices choicesP;
		Dataset dataset;
		int managerId;

		managerId = super.getRequest().getPrincipal().getActiveRoleId();

		userStories = this.repository.findManyUserStoriesByManagerId(managerId);
		choicesUS = SelectChoices.from(userStories, "title", object.getUserStory());

		projects = this.repository.findCreatedProjectsByManagerId(managerId);
		choicesP = SelectChoices.from(projects, "code", object.getProject());

		dataset = super.unbind(object, "userStory", "project");
		dataset.put("userStory", choicesUS.getSelected().getKey());
		dataset.put("userStories", choicesUS);
		dataset.put("project", choicesP.getSelected().getKey());
		dataset.put("projects", choicesP);

		super.getResponse().addData(dataset);
	}
}
