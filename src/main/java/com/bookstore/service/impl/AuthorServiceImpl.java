package com.bookstore.service.impl;

import com.bookstore.common.Messages;
import com.bookstore.common.Pagination;
import com.bookstore.controller.dto.AuthorDTO;
import com.bookstore.entity.Author;
import com.bookstore.exception.EntityNotFoundException;
import com.bookstore.repository.AuthorRepository;
import com.bookstore.repository.specification.AuthorSpecifications;
import com.bookstore.controller.request.AuthorSearchRequest;
import com.bookstore.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class AuthorServiceImpl implements AuthorService, Pagination {

    @Autowired
    private AuthorRepository authorRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<AuthorDTO> searchAuthor(AuthorSearchRequest searchRequest) {
        Specification<Author> query = AuthorSpecifications.generateQuery(searchRequest);
        PageRequest page = PageRequest.of(Optional.ofNullable(searchRequest.getPage()).orElse(DEFAULT_PAGE), Optional.ofNullable(searchRequest.getLimit()).orElse(DEFAULT_LIMIT));
        Page<Author> authors = authorRepository.findAll(query, page);

        return getAuthorDTOList(authors);
    }

    @Override
    public AuthorDTO updateAuthor(AuthorDTO authorDTO, Long authorId) {
        if (!isAuthorValid(authorId))
            throw new EntityNotFoundException(String.format(Messages.AUTHOR_ID_NOT_FOUND, authorId));

        authorDTO.setId(authorId);
        return saveAuthor(authorDTO);
    }

    @Override
    public AuthorDTO saveAuthor(AuthorDTO author) {
        Author savedAuthor = authorRepository.saveAndFlush(author.toEntity());

        return savedAuthor.toDTO();
    }

    @Override
    public void deleteAuthorById(Long id) {
        authorRepository.deleteById(id);
    }

    private Page<AuthorDTO> getAuthorDTOList(Page<Author> authors) {
        return new PageImpl<>(authors.stream()
                .map(Author::toDTO)
                .collect(Collectors.toList()));
    }

    @Transactional(readOnly = true)
    private boolean isAuthorValid(Long id) {
        return authorRepository.findById(id).isPresent();
    }
}
