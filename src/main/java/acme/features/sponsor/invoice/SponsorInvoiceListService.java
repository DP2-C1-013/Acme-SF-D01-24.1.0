
package acme.features.sponsor.invoice;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.invoice.Invoice;
import acme.entities.sponsorship.Sponsorship;
import acme.roles.Sponsor;

@Service
public class SponsorInvoiceListService extends AbstractService<Sponsor, Invoice> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private SponsorInvoiceRepository repository;

	// AbstractService<Sponsor, Sponsorship> ---------------------------


	@Override
	public void authorise() {
		boolean status;
		int sponsorshipId;
		int sponsorId;

		sponsorshipId = this.getRequest().getData("sponsorshipId", int.class);
		sponsorId = this.getRequest().getPrincipal().getActiveRoleId();

		Sponsorship sponsorship = this.repository.findOneSponsorshipById(sponsorshipId);

		boolean validSponsorshipId = sponsorship.getSponsor().getId() == sponsorId;

		status = super.getRequest().getPrincipal().hasRole(Sponsor.class) && validSponsorshipId;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<Invoice> objects;
		int sponsorshipId;

		sponsorshipId = this.getRequest().getData("sponsorshipId", int.class);

		objects = this.repository.findManyInvoicesBySponsorshipId(sponsorshipId);

		super.getBuffer().addData(objects);
	}

	@Override
	public void unbind(final Invoice object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "code", "registrationTime", "draftMode");
		dataset.put("totalAmount", object.totalAmount());
		dataset.put("sponsorship", object.getSponsorship().getCode());

		super.getResponse().addData(dataset);
	}

	@Override
	public void unbind(final Collection<Invoice> objects) {
		assert objects != null;

		int sponsorshipId;

		sponsorshipId = this.getRequest().getData("sponsorshipId", int.class);
		Sponsorship sponsorship = this.repository.findOneSponsorshipById(sponsorshipId);

		super.getResponse().addGlobal("sponsorshipId", sponsorshipId);
		super.getResponse().addGlobal("showCreate", sponsorship.isDraftMode());

	}

}
