package acme.features.inventor.quantity;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.configuration.Configuration;
import acme.entities.item.Item;
import acme.entities.item.ItemType;
import acme.entities.item.Status;
import acme.entities.quantity.Quantity;
import acme.features.administrator.configurations.AdministratorConfigurationRepository;
import acme.framework.components.models.Model;
import acme.framework.controllers.Errors;
import acme.framework.controllers.Request;
import acme.framework.services.AbstractUpdateService;
import acme.roles.Inventor;

@Service
public class InventorQuantityUpdateService implements AbstractUpdateService<Inventor, Quantity>{

	@Autowired
	protected InventorQuantityRepository repository;
	
	@Autowired
	protected AdministratorConfigurationRepository confRepository;
	
	@Override
	public boolean authorise(final Request<Quantity> request) {
		assert request != null;
		final Inventor i = this.repository.findInventorByUserId(request.getPrincipal().getAccountId());
		final Quantity q = this.repository.findOneQuantityById(request.getModel().getInteger("id"));
		
		return q.getToolkit().getInventor().getId()==i.getId();
	}

	@Override
	public void bind(final Request<Quantity> request, final Quantity entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;
		
		request.bind(entity, errors, "number", "item.name", "item.code", "item.technology", "item.description", "item.retailPrice", "item.info", "item.type");
		
	}

	@Override
	public void unbind(final Request<Quantity> request, final Quantity entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;
		
		request.unbind(entity, model, "number", "item.name", "item.code", "item.technology", "item.description", "item.retailPrice", "item.info", "item.status", "item.type");
		
		model.setAttribute("item", entity.getItem());
	}

	@Override
	public Quantity findOne(final Request<Quantity> request) {
		
		return this.repository.findOneQuantityById(request.getModel().getInteger("id"));
	}

	@Override
	public void validate(final Request<Quantity> request, final Quantity entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;
		
		final Item i = entity.getItem();
		errors.state(request, !(i.getType()==ItemType.TOOL && entity.getNumber()>1), "number", "inventor.quantity.number.tool");
		errors.state(request, !(entity.getToolkit().getStatus().toString().equals("PUBLISHED")), "number", "inventor.quantity.toolkit.noPublished");
		errors.state(request, !(entity.getItem().getStatus()==Status.PUBLISHED), "item.status", "inventor.quantity.item.status");
		
		final Collection<Configuration> conf = this.confRepository.findConfigurations();
		for(final Configuration c : conf) {
			errors.state(request, !c.isSpamStrong(i.getCode()), "item.code", "inventor.quantity.item.code.strongSpam");
			errors.state(request, !c.isSpamStrong(i.getDescription()), "item.description", "inventor.quantity.item.description.strongSpam");
			errors.state(request, !c.isSpamStrong(i.getInfo()), "item.info", "inventor.quantity.item.info.strongSpam");
			errors.state(request, !c.isSpamStrong(i.getName()), "item.name", "inventor.quantity.item.name.strongSpam");
			errors.state(request, !c.isSpamStrong(i.getTechnology()), "item.technology", "inventor.quantity.item.technology.strongSpam");
			
			errors.state(request, !c.isSpamWeak(i.getCode()), "item.code", "inventor.quantity.item.code.weakSpam");
			errors.state(request, !c.isSpamWeak(i.getDescription()), "item.description", "inventor.quantity.item.description.weakSpam");
			errors.state(request, !c.isSpamWeak(i.getInfo()), "item.info", "inventor.quantity.item.info.weakSpam");
			errors.state(request, !c.isSpamWeak(i.getName()), "item.name", "inventor.quantity.item.name.weakSpam");
			errors.state(request, !c.isSpamWeak(i.getTechnology()), "item.technology", "inventor.quantity.item.technology.weakSpam");
			
		}
		
	}

	@Override
	public void update(final Request<Quantity> request, final Quantity entity) {
		assert request != null;
		assert entity != null;
		
		final Item i = entity.getItem();
		this.repository.save(i);
		this.repository.save(entity);
		
		
	}

}
