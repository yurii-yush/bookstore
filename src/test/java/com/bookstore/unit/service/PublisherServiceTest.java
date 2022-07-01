package com.bookstore.unit.service;

import com.bookstore.common.Pagination;
import com.bookstore.controller.dto.PublisherDTO;
import com.bookstore.controller.request.PublisherSearchRequest;
import com.bookstore.entity.Publisher;
import com.bookstore.exception.EntityNotFoundException;
import com.bookstore.repository.PublisherRepository;
import com.bookstore.service.impl.PublisherServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class PublisherServiceTest {

    @Mock
    private PublisherRepository publisherRepository;

    @InjectMocks
    private PublisherServiceImpl publisherService;

    private Publisher publisher1, publisher2, publisher3, publisher4;

    @BeforeEach
    public void setup() {
        publisher1 = new Publisher(1L, "Видавництво Старого Лева", "UA");
        publisher2 = new Publisher(2L, "Книги - XXI", "PL");
        publisher3 = new Publisher(3L, "Meridian Czernowitz", "GB");
        publisher4 = new Publisher(4L, "New Time Харків", "UA");
    }

    @Test
    public void givenValidSearchRequestWithoutPagination_whenSearchPublisher_thenReturnPageWithPublishers() {
        //given
        PublisherSearchRequest searchRequest = new PublisherSearchRequest(null, "UA");

        PageRequest page = PageRequest.of(Pagination.DEFAULT_PAGE, Pagination.DEFAULT_LIMIT);
        Page<Publisher> pageFromDB = new PageImpl<>(List.of(publisher1, publisher2), page, 2L);
        Page<PublisherDTO> expectedPage = getPageDTOList(new PageImpl<>(List.of(publisher1, publisher2), page, 2L));

        //when
        Page<PublisherDTO> actualPage = doSearchPublisher(pageFromDB, searchRequest);

        //then
        assertThat(actualPage, equalTo(expectedPage));
    }

    @Test
    public void givenValidSearchRequestWithPagination_whenSearchPublisher_thenReturnPageWithPublishers() {
        //given
        PublisherSearchRequest searchRequest = new PublisherSearchRequest("shchyshyn", null);
        searchRequest.setPage(0);
        searchRequest.setLimit(2);

        PageRequest page = PageRequest.of(searchRequest.getPage(), searchRequest.getLimit());
        Page<Publisher> pageFromDB = new PageImpl<>(List.of(publisher1, publisher4), page, 2L);
        Page<PublisherDTO> expectedPage = getPageDTOList(pageFromDB);

        //when
        Page<PublisherDTO> actualPage = doSearchPublisher(pageFromDB, searchRequest);

        //then
        assertThat(actualPage, equalTo(expectedPage));
    }

    @Test
    public void givenValidPublisher_whenSavePublisher_thenSaveAndReturnPublisher() throws Exception {
        //given
        when(publisherRepository.saveAndFlush(Mockito.any()))
                .thenReturn(publisher1);

        //when
        PublisherDTO actualPublisher = publisherService.savePublisher(publisher1.toDTO());

        //then
        assertThat(publisher1.toDTO(), equalTo(actualPublisher));
    }

    @Test
    public void givenPublisherId_whenDeletePublisher_thenDelete_andStatus200() throws Exception {
        //given
        long PublisherId = 1L;

        //when
        publisherService.deletePublisherById(PublisherId);

        //then
        verify(publisherRepository, times(1)).deleteById(PublisherId);
    }

    @Test
    public void givenValidPublisher_whenUpdatePublisher_thenUpdateAndReturnPublisher() {
        //given
        when(publisherRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(publisher1));

        when(publisherRepository.saveAndFlush(Mockito.any()))
                .thenReturn(publisher1);

        //when
        PublisherDTO actualPublisher = publisherService.updatePublisher(publisher1.toDTO(), publisher1.getId());

        //then
        assertThat(publisher1.toDTO(), equalTo(actualPublisher));
    }

    @Test
    public void givenNotValidPublisherId_whenUpdatePublisher_thenThrowEntityNotFoundException() {
        //when
        when(publisherRepository.findById(Mockito.any()))
                .thenReturn(Optional.empty());

        //then
        assertThrows(EntityNotFoundException.class, () -> publisherService.updatePublisher(publisher1.toDTO(), publisher1.getId()));//todo check message
    }

    private Page<PublisherDTO> doSearchPublisher(Page<Publisher> pageFromDB, PublisherSearchRequest searchRequest) {
        when(publisherRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(pageFromDB);


        return publisherService.searchPublisher(searchRequest);
    }

    private Page<PublisherDTO> getPageDTOList(Page<Publisher> Publishers) {
        return new PageImpl<>(Publishers.stream()
                .map(Publisher::toDTO)
                .collect(Collectors.toList()));
    }
}
