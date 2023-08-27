package com.example.bookms.controller;

import com.example.bookms.model.Book;
import com.example.bookms.repository.BookRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
public class BookResource {
    @Autowired
    public BookRepo bookrepo;

    public Logger logger= LoggerFactory.getLogger(BookResource.class);
    @GetMapping("/books")
    public List<Book> getAllBooks(){
        return bookrepo.findAll();
    }

    @GetMapping("/books/{id}")
    public ResponseEntity<Book> getAllBooks(@PathVariable int id){

        Optional<Book> book= bookrepo.findById(id);
        if (book.isPresent()) {
            logger.info("book found");
            return ResponseEntity.ok(book.get());
        }

        logger.error("book not found: {}", id);
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/books")
    public ResponseEntity<Book> addBook(@RequestBody Book book) throws URISyntaxException {
       Book addedBook =  bookrepo.save(book);
        logger.info("book added");
        ResponseEntity<Book> saved= ResponseEntity.created(new URI(Integer.toString(addedBook.getIsbn()))).body(addedBook);
        return saved;
    }


    @PostMapping("/update-book")
    public ResponseEntity<Book> updateBook(@RequestBody Book book) throws URISyntaxException {
        Optional<Book> bookFound =  bookrepo.findById(book.getIsbn());
        if(bookFound.isPresent() ){
            logger.info("book found");
            Book addedBook =  bookrepo.save(book);
            ResponseEntity<Book> saved= ResponseEntity.created(new URI(Integer.toString(addedBook.getIsbn()))).body(addedBook);
            return saved;
        }
        logger.error("book not found: {}", book.getIsbn());
        return ResponseEntity.notFound().build();

    }

    //update book such that the issued copy is increased by 1
    @GetMapping("/issue-book/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable int id) throws URISyntaxException {
        Optional<Book> bookFound =  bookrepo.findById(id);
        if(bookFound.isPresent() ){
            if(bookFound.get().getTotalCopies() - bookFound.get().getIssuedCopies() >0){
                logger.info("book found and can be issued");
                Book book= bookFound.get();
                book.setIssuedCopies(bookFound.get().getIssuedCopies()+1);
                Book addedBook =  bookrepo.save(book);
                return ResponseEntity.ok(addedBook);

            }

        }
        logger.error("book not found: {}", id);
        return ResponseEntity.notFound().build();

    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable int id){
        Optional<Book> bookFound =  bookrepo.findById(id);
        if(bookFound.isPresent()){
            logger.info("book found");
            bookrepo.deleteById(id);
            return ResponseEntity.ok("book deleted");

        }
        logger.error("book not found: {}", id);
        return ResponseEntity.notFound().build();
    }
}
