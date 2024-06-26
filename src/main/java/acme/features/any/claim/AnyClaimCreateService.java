
package acme.features.any.claim;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Any;
import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.entities.claim.Claim;

@Service
public class AnyClaimCreateService extends AbstractService<Any, Claim> {

	@Autowired
	protected AnyClaimRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Claim claim = new Claim();

		final Date instantiation = MomentHelper.getCurrentMoment();

		claim.setInstantiationMoment(instantiation);

		super.getBuffer().addData(claim);
	}

	@Override
	public void bind(final Claim object) {
		assert object != null;

		super.bind(object, "code", "heading", "description", "department", "email", "link");
	}

	@Override
	public void validate(final Claim object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("confirmation")) {
			final boolean confirmation = super.getRequest().getData("confirmation", boolean.class);

			super.state(confirmation, "confirmation", "error.confirmation.required");
		}

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			final boolean duplicatedCode = this.repository.findAllClaims().stream().anyMatch(e -> e.getCode().equals(object.getCode()));

			super.state(!duplicatedCode, "code", "any.claim.form.error.duplicated-code");
		}

	}

	@Override
	public void perform(final Claim object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Claim object) {
		assert object != null;

		final Dataset dataset = super.unbind(object, "code", "heading", "description", "department", "email", "link");

		dataset.put("confirmation", false);

		super.getResponse().addData(dataset);
	}
}
