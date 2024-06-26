
package acme.features.sponsor.invoice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.invoice.Invoice;
import acme.roles.Sponsor;

@Service
public class SponsorInvoiceShowService extends AbstractService<Sponsor, Invoice> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private SponsorInvoiceRepository repository;

	// AbstractService<Authenticated, Claim> ---------------------------


	@Override
	public void authorise() {
		boolean status;

		var request = super.getRequest();

		int sponsorId;
		int invoiceId;

		invoiceId = request.getData("id", int.class);

		sponsorId = request.getPrincipal().getActiveRoleId();

		Invoice object = this.repository.findOneInvoiceById(invoiceId);

		status = object != null && request.getPrincipal().hasRole(Sponsor.class) && object.getSponsorship().getSponsor().getId() == sponsorId;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Invoice object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneInvoiceById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void unbind(final Invoice object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "code", "registrationTime", "dueDate", "quantity", "tax", "link", "draftMode");
		dataset.put("sponsorship", object.getSponsorship().getCode());
		dataset.put("totalAmount", object.totalAmount());
		dataset.put("sponsorshipDraftMode", object.getSponsorship().isDraftMode());

		super.getResponse().addData(dataset);
	}
}
