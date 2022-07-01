package com.bookstore.service;

import com.bookstore.controller.dto.PublisherDTO;
import com.bookstore.controller.request.PublisherSearchRequest;
import org.springframework.data.domain.Page;

public interface PublisherService {
    Page<PublisherDTO> searchPublisher(PublisherSearchRequest searchRequest);

    PublisherDTO updatePublisher(PublisherDTO publisherDTO, Long id);

    PublisherDTO savePublisher(PublisherDTO author);

    void deletePublisherById(Long id);
}
