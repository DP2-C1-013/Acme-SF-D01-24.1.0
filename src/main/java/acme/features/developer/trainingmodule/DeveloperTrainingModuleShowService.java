
package acme.features.developer.trainingmodule;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.project.Project;
import acme.entities.trainingmodule.DifficultyLevel;
import acme.entities.trainingmodule.TrainingModule;
import acme.entities.trainingsession.TrainingSession;
import acme.roles.Developer;

@Service
public class DeveloperTrainingModuleShowService extends AbstractService<Developer, TrainingModule> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private DeveloperTrainingModuleRepository repository;

	// AbstractService<Developer, TrainingModule> ---------------------------


	public Integer getEstimatedTotalTime(final TrainingModule tm) {
		int estimatedTotalTime = 0;
		List<TrainingSession> ts = this.repository.findTrainingSessionsByTMId(tm.getId()).stream().toList();

		for (TrainingSession t : ts)
			estimatedTotalTime += (t.getEndDate().getTime() - t.getStartDate().getTime()) / 3600000;
		return estimatedTotalTime;
	}

	@Override
	public void authorise() {
		boolean status;

		var request = super.getRequest();

		int developerId;
		int trainingModuleId;

		trainingModuleId = request.getData("id", int.class);
		developerId = request.getPrincipal().getActiveRoleId();

		TrainingModule object = this.repository.findTrainingModuleById(trainingModuleId);

		status = object != null && request.getPrincipal().hasRole(Developer.class) && object.getDeveloper().getId() == developerId;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		TrainingModule object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findTrainingModuleById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void unbind(final TrainingModule object) {
		assert object != null;

		SelectChoices choices;
		SelectChoices projects;
		Dataset dataset;

		choices = SelectChoices.from(DifficultyLevel.class, object.getDifficultyLevel());
		if (object.isDraftMode())
			projects = SelectChoices.from(this.repository.findAllProjectsDraftModeFalse(), "code", object.getProject());
		else {
			Collection<Project> project = List.of(object.getProject());
			projects = SelectChoices.from(project, "code", object.getProject());
		}

		dataset = super.unbind(object, "code", "creationMoment", "details", "difficultyLevel", "updateMoment", "optionalLink", "draftMode");
		dataset.put("difficultyLevels", choices);
		dataset.put("estimatedTotalTime", this.getEstimatedTotalTime(object));
		dataset.put("project", projects.getSelected().getKey());
		dataset.put("projects", projects);
		dataset.put("projectDraftMode", object.getProject().isDraftMode());

		super.getResponse().addData(dataset);
	}
}
