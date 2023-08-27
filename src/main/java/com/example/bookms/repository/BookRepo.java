package com.example.bookms.repository;

import com.example.bookms.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Repository
public interface BookRepo extends JpaRepository<Book,Integer>{


}
