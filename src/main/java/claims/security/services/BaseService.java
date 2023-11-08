package claims.security.services;

import claims.security.exceptions.NotFoundException;
import claims.security.repositories.BaseRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public abstract class BaseService<T> {

	@Autowired
	BaseRepository<T, String> baseRepository;


	public Optional<T> findById(String id) {
		return baseRepository.findById(id);
	}

	public void deleteById(String id) {

		Optional<T> result = this.baseRepository.findById(id);

		if(!result.isPresent()){
			throw  new NotFoundException("ID not found in database");
		}
		baseRepository.deleteById(id);
		baseRepository.flush();
	}

	public T save(T entity) {
		 return baseRepository.saveAndFlush(entity);

	}

	public boolean existById(String id) {
		return baseRepository.existsById(id);
	}

	public T saveAndFlush(T entity) {
		return baseRepository.saveAndFlush(entity);
	}

	public <T> List<T> findAll() {
		return (List<T>) baseRepository.findAll();
	}






}
