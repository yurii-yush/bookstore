package com.bookstore.service.impl;

import com.bookstore.common.Messages;
import com.bookstore.common.Pagination;
import com.bookstore.controller.dto.PublisherDTO;
import com.bookstore.entity.Publisher;
import com.bookstore.exception.EntityNotFoundException;
import com.bookstore.repository.PublisherRepository;
import com.bookstore.repository.specification.PublisherSpecifications;
import com.bookstore.controller.request.PublisherSearchRequest;
import com.bookstore.service.PublisherService;
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
public class PublisherServiceImpl implements PublisherService, Pagination {

    @Autowired
    PublisherRepository publisherRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<PublisherDTO> searchPublisher(PublisherSearchRequest searchRequest) {
        Specification<Publisher> query = PublisherSpecifications.generateQuery(searchRequest);
        PageRequest page = PageRequest.of(Optional.ofNullable(searchRequest.getPage()).orElse(DEFAULT_PAGE), Optional.ofNullable(searchRequest.getLimit()).orElse(DEFAULT_LIMIT));
        Page<Publisher> publishers = publisherRepository.findAll(query, page);
        return getPublisherDTOList(publishers);
    }

    @Override
    public PublisherDTO updatePublisher(PublisherDTO publisherDTO, Long publisherId) {
        if (!isPublisherValid(publisherId)) {
            throw new EntityNotFoundException(String.format(Messages.PUBLISHER_ID_NOT_FOUND, publisherId));
        }

        publisherDTO.setId(publisherId);
        return savePublisher(publisherDTO);
    }

    @Override
    public PublisherDTO savePublisher(PublisherDTO publisher) {
        Publisher savedPublisher = publisherRepository.saveAndFlush(publisher.toEntity());
        return savedPublisher.toDTO();
    }

    @Override
    public void deletePublisherById(Long id) {
        publisherRepository.deleteById(id);
    }

    private Page<PublisherDTO> getPublisherDTOList(Page<Publisher> publishers) {
        return new PageImpl<>(publishers.stream()
                .map(Publisher::toDTO)
                .collect(Collectors.toList()));
    }

    @Transactional(readOnly = true)
    private boolean isPublisherValid(Long id) {
        return publisherRepository.findById(id).isPresent();
    }
}
