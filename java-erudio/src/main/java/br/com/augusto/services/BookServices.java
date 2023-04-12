package br.com.augusto.services;

import br.com.augusto.controllers.BookController;
import br.com.augusto.exceptions.RequiredObjectIsNullException;
import br.com.augusto.exceptions.ResourceNotFoundException;
import br.com.augusto.mapper.DozerMapper;
import br.com.augusto.model.Book;
import br.com.augusto.repositories.BookRepository;
import br.com.augusto.vo.v1.BookVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class BookServices {


    private Logger logger = Logger.getLogger(BookServices.class.getName());

    @Autowired
    BookRepository bookRepository;

    public List<BookVO> findAll(){
        logger.info("Finding all book!");

        var books = DozerMapper.parseListObjects(bookRepository.findAll(),BookVO.class ) ;
        books.forEach(p -> p.add(linkTo(methodOn(BookController.class).finById(p.getKey())).withSelfRel()));
        return books;
    }

    public BookVO findById(Long id){
        logger.info("Finding one book!");

        var entity =  bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No record found for this ID!"));
       var vo =  DozerMapper.parseObject(entity,BookVO.class);
       vo.add(linkTo(methodOn(BookController.class).finById(id)).withSelfRel());
       return vo;
    }

    public BookVO create(BookVO book) {

        if(book == null) throw new RequiredObjectIsNullException();


        logger.info("Creating one book!");
        var entity = DozerMapper.parseObject(book,Book.class);
        var vo = DozerMapper.parseObject (bookRepository.save(entity),BookVO.class);
        vo.add(linkTo(methodOn(BookController.class).finById(vo.getKey())).withSelfRel());
        return vo;    }


    public BookVO update(BookVO book) {
        if(book == null) throw new RequiredObjectIsNullException();
        logger.info("Updating one book!");

       var entity = bookRepository.findById(book.getKey())
                .orElseThrow(() -> new ResourceNotFoundException("No record found for this ID!"));

        entity.setAuthor(book.getAuthor());
        entity.setLaunchDate(book.getLaunchDate());
        entity.setPrice(book.getPrice());
        entity.setTitle(book.getTitle());
        var vo = DozerMapper.parseObject (bookRepository.save(entity),BookVO.class);
        vo.add(linkTo(methodOn(BookController.class).finById(vo.getKey())).withSelfRel());
        return vo;
    }

    public void delete(Long id) {
        logger.info("Deleting one book!");

        var entity = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No record found for this ID!"));

         bookRepository.delete(entity);
    }

}
