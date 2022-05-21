
package acme.features.inventor.patronageReports;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.configuration.Configuration;
import acme.entities.patronage.Patronage;
import acme.entities.patronagereport.PatronageReport;
import acme.features.administrator.configurations.AdministratorConfigurationRepository;
import acme.framework.components.models.Model;
import acme.framework.controllers.Errors;
import acme.framework.controllers.Request;
import acme.framework.services.AbstractCreateService;
import acme.roles.Inventor;

@Service
public class InventorPatronageReportCreateService implements AbstractCreateService<Inventor, PatronageReport> {

	@Autowired
	protected InventorPatronageReportRepository repository;
	@Autowired
	protected AdministratorConfigurationRepository	configurationRepository;


	@Override
	public boolean authorise(final Request<PatronageReport> request) {
		assert request != null;

		return true;
	}

	@Override
	public PatronageReport instantiate(final Request<PatronageReport> request) {
		assert request != null;

		PatronageReport result;
		int patronageId;
		Patronage patronage;
		Date date;
		String numSeqPatronageReports;
		int patronageReportId;

		patronageId = request.getModel().getInteger("id");
		patronage = this.repository.findPatronageById(patronageId);
		date = new Date();
		patronageReportId = request.getModel().getInteger("id");

		numSeqPatronageReports = Integer.toString(this.repository.findPatronageReportById2(patronageReportId).size() + 1);

		result = new PatronageReport();
		result.setPatronage(patronage);
		result.setCreatedAt(date);
		result.setSeqNumber("abc-00" + numSeqPatronageReports);

		return result;
	}

	@Override
	public void bind(final Request<PatronageReport> request, final PatronageReport entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;

		request.bind(entity, errors, "seqNumber", "createdAt", "memorandum", "link");

	}

	@Override
	public void validate(final Request<PatronageReport> request, final PatronageReport entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;

		final Configuration config = this.configurationRepository.findConfiguration();

		errors.state(request, !config.isSpamStrong(entity.getMemorandum()), "memorandum", "administrator.announcement.strongspam");
		errors.state(request, !config.isSpamWeak(entity.getMemorandum()), "memorandum", "administrator.announcement.weakspam");
		errors.state(request, !config.isSpamStrong(entity.getLink()), "link", "administrator.announcement.strongspam");
		errors.state(request, !config.isSpamWeak(entity.getLink()), "link", "administrator.announcement.weakspam");
		
		final boolean confirm = request.getModel().getBoolean("confirm");
		errors.state(request, confirm, "confirm", "inventor.patronage-report.form.accept.error");
	}

	@Override
	public void unbind(final Request<PatronageReport> request, final PatronageReport entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;

		request.unbind(entity, model, "seqNumber", "createdAt", "memorandum", "link");
		model.setAttribute("id", request.getModel().getAttribute("id"));
		model.setAttribute("confirm", "false");
	}

	@Override
	public void create(final Request<PatronageReport> request, final PatronageReport entity) {
		assert request != null;
		assert entity != null;

		this.repository.save(entity);

	}

}
