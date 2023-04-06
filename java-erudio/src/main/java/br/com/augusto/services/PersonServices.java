package br.com.augusto.services;

import br.com.augusto.controllers.PersonController;
import br.com.augusto.exceptions.RequiredObjectIsNullException;
import br.com.augusto.exceptions.ResourceNotFoundException;
import br.com.augusto.mapper.DozerMapper;
import br.com.augusto.mapper.custom.PersonMapper;
import br.com.augusto.model.Person;
import br.com.augusto.repositories.PersonRepository;
import br.com.augusto.vo.v1.PersonVO;
import br.com.augusto.vo.v2.PersonVOV2;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
public class PersonServices {


    private Logger logger = Logger.getLogger(PersonServices.class.getName());

    @Autowired
    PersonRepository personRepository;

    @Autowired
    PersonMapper mapper;

    public List<PersonVO> findAll(){
        logger.info("Finding all people!");

        var persons = DozerMapper.parseListObjects(personRepository.findAll(), PersonVO.class ) ;
        persons.forEach(p -> p.add(linkTo(methodOn(PersonController.class).finById(p.getKey())).withSelfRel()));
        return persons;
    }

    public PersonVO findById(Long id){
        logger.info("Finding one person!");

        var entity =  personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No record found for this ID!"));
       var vo =  DozerMapper.parseObject(entity, PersonVO.class);
       vo.add(linkTo(methodOn(PersonController.class).finById(id)).withSelfRel());
       return vo;
    }

    public PersonVO create(PersonVO person) {

        if(person == null) throw new RequiredObjectIsNullException();


        logger.info("Creating one person!");
        var entity = DozerMapper.parseObject(person, Person.class);
        var vo = DozerMapper.parseObject (personRepository.save(entity), PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).finById(vo.getKey())).withSelfRel());
        return vo;    }

    public PersonVOV2 createV2(PersonVOV2 person) {
        logger.info("Creating one person!");
        var entity = mapper.convertVoToEntity(person);
        var vo = mapper.convertEntityToVo(personRepository.save(entity));
        return vo;
    }

    public PersonVO update(PersonVO person) {
        if(person == null) throw new RequiredObjectIsNullException();
        logger.info("Updating one person!");

       var entity = personRepository.findById(person.getKey())
                .orElseThrow(() -> new ResourceNotFoundException("No record found for this ID!"));

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());
        var vo = DozerMapper.parseObject (personRepository.save(entity), PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).finById(vo.getKey())).withSelfRel());
        return vo;
    }

    public void delete(Long id) {
        logger.info("Deleting one person!");

        var entity = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No record found for this ID!"));

         personRepository.delete(entity);
    }

}
