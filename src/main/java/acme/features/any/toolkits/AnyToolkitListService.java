package acme.features.any.toolkits;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.toolkit.Toolkit;
import acme.framework.components.models.Model;
import acme.framework.controllers.Request;
import acme.framework.roles.Any;
import acme.framework.services.AbstractListService;

@Service
public class AnyToolkitListService implements AbstractListService<Any, Toolkit>{
	
	@Autowired
	protected AnyToolkitRepository repository;
	
	@Override
	public boolean authorise(final Request<Toolkit> request) {
		assert request != null;
		
		return true;
	}
	
	@Override
	public Collection<Toolkit> findMany(final Request<Toolkit> request){
		assert request !=null;
		
		Collection<Toolkit> res;
		res=this.repository.findAnyToolkits();
		return res;
	}
	@Override
	public void unbind(final Request<Toolkit> request, final Toolkit entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;
		
		request.unbind(entity, model,"code", "title", "link");
		
	}
}
