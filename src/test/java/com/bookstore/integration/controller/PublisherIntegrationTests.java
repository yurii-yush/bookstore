package com.bookstore.integration.controller;


import com.bookstore.common.Messages;
import com.bookstore.controller.dto.PublisherDTO;
import com.bookstore.controller.request.PublisherSearchRequest;
import com.bookstore.entity.Publisher;
import com.bookstore.integration.AbstractIT;
import com.bookstore.repository.PublisherRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc

public class PublisherIntegrationTests extends AbstractIT {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PublisherRepository publisherRepository;
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void givenValidPublisher_whenSavePublisher_thenReturnURI_andStatus201() throws Exception {
        //given
        long maxId = getMaxId() + 1;

        Publisher expectedPublisher = new Publisher(maxId, "Видавництво", "UA");
        URI location = URI.create(String.format(Messages.CREATED_PUBLISHER_URI, maxId));

        //when
        mockMvc.perform(
                        post(Messages.PUBLISHER_CONTROLLER_URI)
                                .content(objectMapper.writeValueAsString(expectedPublisher.toDTO()))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(location)));

        //then
        Optional<Publisher> actualPublisher = publisherRepository.findById(expectedPublisher.getId());
        assertThat(expectedPublisher, equalTo(actualPublisher.get()));
    }

    @Test
    public void givenPublisherId_whenDeletePublisher_thenDeletePublisher_andStatus200() throws Exception {
        //given
        addPublishers();
        Publisher expectedPublisher = getSinglePublisher();

        //when
        mockMvc.perform(
                        delete(Messages.PUBLISHER_CONTROLLER_URI + Messages.ID_MAPPING, expectedPublisher.getId()))
                .andExpect(status().isOk());

        //then
        Optional<Publisher> actualPublisher = publisherRepository.findById(expectedPublisher.getId());
        assertThat(actualPublisher, equalTo(Optional.empty()));
    }

    @Test
    public void givenSurname_whenSearchPublisher_thenReturnListOfPublishers_andStatus200() throws Exception {
        //given
        String title = "Meridian";
        PublisherSearchRequest searchRequest = new PublisherSearchRequest();
        searchRequest.setTitle(title);

        //when
        List<Publisher> allPublishers = addPublishers();
        List<PublisherDTO> expectedPublishers = filterPublishersByTitle(allPublishers, title);

        //then
        performPublisherSearch(searchRequest, expectedPublishers);
    }

    @Test
    public void givenValidCountry_whenSearchPublisher_thenReturnListOfPublishers_andStatus200() throws Exception {
        //given
        String country = "UA";
        PublisherSearchRequest searchRequest = new PublisherSearchRequest();
        searchRequest.setCountry(country);

        //when
        List<Publisher> allPublishers = addPublishers();
        List<PublisherDTO> expectedPublishers = filterPublishersByCountry(allPublishers, country);

        //then
        performPublisherSearch(searchRequest, expectedPublishers);
    }

    @Test
    public void givenValidPublisher_whenUpdatePublisher_thenReturnURI_andStatus200() throws Exception {
        //given
        addPublishers();
        Publisher expectedPublisher = getSinglePublisher();
        expectedPublisher.setTitle("Median");

        //when
        mockMvc.perform(
                        put(Messages.PUBLISHER_CONTROLLER_URI + Messages.ID_MAPPING, expectedPublisher.getId())
                                .content(objectMapper.writeValueAsString(expectedPublisher.toDTO()))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedPublisher.toDTO())));

        //then
        Optional<Publisher> actualPublisher = publisherRepository.findById(expectedPublisher.getId());
        assertThat(expectedPublisher, equalTo(actualPublisher.get()));
    }

    private void performPublisherSearch(PublisherSearchRequest searchRequest, List<PublisherDTO> expectedPublishers) throws Exception {
        //given
        Page<PublisherDTO> expectedPage = new PageImpl<>(expectedPublishers);

        //when
        mockMvc.perform(
                        get(Messages.PUBLISHER_CONTROLLER_URI)
                                .content(objectMapper.writeValueAsString(searchRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedPage)));
    }

    private List<PublisherDTO> filterPublishersByCountry(List<Publisher> allPublishers, String country) {
        return allPublishers.stream()
                .filter(Publisher -> Publisher.getCountry().contains(country))
                .map(Publisher::toDTO)
                .collect(Collectors.toList());
    }

    private List<PublisherDTO> filterPublishersByTitle(List<Publisher> allPublishers, String title) {
        return allPublishers.stream()
                .filter(Publisher -> Publisher.getTitle().contains(title))
                .map(Publisher::toDTO)
                .collect(Collectors.toList());
    }

    private List<Publisher> addPublishers() {
        Publisher publisher1 = new Publisher(1L, "Видавництво Старого Лева", "UA");
        Publisher publisher2 = new Publisher(2L, "Книги - XXI", "PL");
        Publisher publisher3 = new Publisher(3L, "Meridian Czernowitz", "GB");
        Publisher publisher4 = new Publisher(4L, "New Time Харків", "UA");
        Publisher publisher5 = new Publisher(5L, "Port-Royal", "UA");
        List<Publisher> publishers = List.of(publisher1, publisher2, publisher3, publisher4, publisher5);
        publisherRepository.deleteAll();

        return publisherRepository.saveAllAndFlush(publishers);
    }

    private Publisher getSinglePublisher() {
        return publisherRepository.findAll().get(0);
    }

    private long getMaxId() {
        Publisher previousPublisher = new Publisher(1L, "Видавництво Старого Лева", "UA");

        return publisherRepository.saveAndFlush(previousPublisher).getId();
    }
}
