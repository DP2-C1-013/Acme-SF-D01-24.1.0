
package acme.features.auditor.auditrecord;

import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.auditrecord.AuditMark;
import acme.entities.auditrecord.AuditRecord;
import acme.entities.codeaudit.CodeAudit;
import acme.roles.Auditor;

@Service
public class AuditorAuditRecordUpdateService extends AbstractService<Auditor, AuditRecord> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuditorAuditRecordRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;

		var request = super.getRequest();

		int auditorId;
		int auditRecordId;

		auditRecordId = request.getData("id", int.class);

		auditorId = request.getPrincipal().getActiveRoleId();

		AuditRecord object = this.repository.findOneAuditRecordById(auditRecordId);

		status = object != null && request.getPrincipal().hasRole(Auditor.class) && object.getCodeAudit().getAuditor().getId() == auditorId && object.getDraftMode();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		AuditRecord object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneAuditRecordById(id);

		super.getBuffer().addData(object);

	}

	@Override
	public void bind(final AuditRecord object) {
		assert object != null;

		String markString = this.getRequest().getData("mark", String.class);
		AuditMark mark = AuditMark.parseAuditMark(markString);
		super.bind(object, "code", "startDate", "endDate", "link");
		object.setMark(mark);
	}

	@Override
	public void validate(final AuditRecord object) {
		assert object != null;

		CodeAudit existingCodeAudit = this.repository.findOneCodeAuditById(object.getCodeAudit().getId());

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			AuditRecord existing;
			existing = this.repository.findOneAuditRecordByCode(object.getCode());
			super.state(existing == null || existing.getId() == object.getId(), "duration", "auditor.auditRecord.form.error.duplicated-code");
		}

		if (!(super.getBuffer().getErrors().hasErrors("startDate") || super.getBuffer().getErrors().hasErrors("endDate"))) {
			Date minDate = new Date(99, 12, 31, 23, 59);
			Date minimunDuration;
			minimunDuration = MomentHelper.deltaFromMoment(object.getStartDate(), 1, ChronoUnit.HOURS);
			super.state(MomentHelper.isAfterOrEqual(object.getEndDate(), minimunDuration) && object.getStartDate().after(minDate), "endDate", "auditor.auditRecord.form.error.invalid-dates");
		}

		if (!super.getBuffer().getErrors().hasErrors("codeAudit"))
			super.state(existingCodeAudit != null && existingCodeAudit.getDraftMode() && !existingCodeAudit.getProject().isDraftMode(), "codeAudit", "auditor.codeAudit.form.error.codeAudit-draft-mode-is-set-to-false");

	}

	@Override
	public void perform(final AuditRecord object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final AuditRecord object) {
		assert object != null;

		Dataset dataset;
		AuditRecord ar = new AuditRecord();

		SelectChoices marks;

		marks = SelectChoices.from(AuditMark.class, ar.getMark());

		dataset = super.unbind(object, "code", "startDate", "endDate", "mark", "link", "draftMode");
		dataset.put("marks", marks);
		dataset.put("codeAudit", object.getCodeAudit().getCode());
		dataset.put("codeAuditDraftMode", object.getCodeAudit().getDraftMode());
		dataset.put("codeAuditId", object.getCodeAudit().getId());

		super.getResponse().addData(dataset);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("PUT"))
			PrincipalHelper.handleUpdate();
	}
}
