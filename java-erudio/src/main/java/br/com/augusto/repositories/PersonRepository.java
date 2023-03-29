package br.com.augusto.repositories;

import br.com.augusto.model.Person;
import br.com.augusto.vo.v1.PersonVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

}
