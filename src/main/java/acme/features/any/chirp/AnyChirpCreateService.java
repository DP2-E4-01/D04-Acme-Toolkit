
package acme.features.any.chirp;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.chirp.Chirp;
import acme.entities.configuration.Configuration;
import acme.features.administrator.configurations.AdministratorConfigurationRepository;
import acme.framework.components.models.Model;
import acme.framework.controllers.Errors;
import acme.framework.controllers.Request;
import acme.framework.roles.Any;
import acme.framework.services.AbstractCreateService;

@Service
public class AnyChirpCreateService implements AbstractCreateService<Any, Chirp> {

	@Autowired
	protected AnyChirpRepository					repository;

	@Autowired
	protected AdministratorConfigurationRepository	configurationRepository;


	@Override
	public boolean authorise(final Request<Chirp> request) {
		assert request != null;
		return true;
	}

	@Override
	public Chirp instantiate(final Request<Chirp> request) {
		assert request != null;
		Chirp res;
		res = new Chirp();
		return res;
	}

	@Override
	public void bind(final Request<Chirp> request, final Chirp entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;

		Date creationTime;
		Calendar calendar;
		creationTime = new Date();
		calendar = Calendar.getInstance();
		creationTime = calendar.getTime();
		request.bind(entity, errors, "author", "body", "email", "title");
		entity.setCreationTime(creationTime);

	}

	@Override
	public void validate(final Request<Chirp> request, final Chirp entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;

		final Configuration config = this.configurationRepository.findConfiguration();

		errors.state(request, !config.isSpamStrong(entity.getAuthor()), "author", "administrator.announcement.strongspam");
		errors.state(request, !config.isSpamWeak(entity.getAuthor()), "author", "administrator.announcement.weakspam");
		errors.state(request, !config.isSpamStrong(entity.getBody()), "body", "administrator.announcement.strongspam");
		errors.state(request, !config.isSpamWeak(entity.getBody()), "body", "administrator.announcement.weakspam");
		errors.state(request, !config.isSpamStrong(entity.getEmail()), "email", "administrator.announcement.strongspam");
		errors.state(request, !config.isSpamWeak(entity.getEmail()), "email", "administrator.announcement.weakspam");
		errors.state(request, !config.isSpamStrong(entity.getTitle()), "title", "administrator.announcement.strongspam");
		errors.state(request, !config.isSpamWeak(entity.getTitle()), "title", "administrator.announcement.weakspam");

		final boolean confirm = request.getModel().getBoolean("confirm");
		errors.state(request, confirm, "confirm", "any.chirp.accept.error");

	}

	@Override
	public void unbind(final Request<Chirp> request, final Chirp entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;

		request.unbind(entity, model, "author", "body", "email", "title");
		model.setAttribute("confirm", false);
	}

	@Override
	public void create(final Request<Chirp> request, final Chirp entity) {
		assert request != null;
		assert entity != null;

		this.repository.save(entity);
	}

}
