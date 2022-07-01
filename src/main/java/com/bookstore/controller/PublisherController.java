package com.bookstore.controller;

import com.bookstore.common.Messages;
import com.bookstore.controller.dto.PublisherDTO;
import com.bookstore.controller.request.PublisherSearchRequest;
import com.bookstore.service.PublisherService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;


@Validated
@RestController
@RequestMapping(value = Messages.PUBLISHER_CONTROLLER_URI)
public class PublisherController {

    @Autowired
    private PublisherService publisherService;

    @ApiOperation(value = "This method is used to search Publishers")
    @GetMapping
    public ResponseEntity<Page<PublisherDTO>> searchPublisher(@Valid @RequestBody PublisherSearchRequest searchRequest) {
        Page<PublisherDTO> publishers = publisherService.searchPublisher(searchRequest);

        return ResponseEntity.status(HttpStatus.OK).body(publishers);
    }

    @ApiOperation(value = "This method is used to save new Publisher")
    @PostMapping
    public ResponseEntity<URI> savePublisher(@Valid @RequestBody PublisherDTO publisher) {
        PublisherDTO createdPublisher = publisherService.savePublisher(publisher);
        URI location = URI.create(String.format(Messages.CREATED_PUBLISHER_URI, createdPublisher.getId()));

        return ResponseEntity.status(HttpStatus.CREATED).body(location);
    }

    @ApiOperation(value = "This method is used to delete Publisher by ID")
    @DeleteMapping(value = Messages.ID_MAPPING)
    public ResponseEntity<?> deletePublisherById(@PathVariable(Messages.ID_PATH) Long id) {
        publisherService.deletePublisherById(id);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ApiOperation(value = "This method is used to update Publisher by ID")
    @PutMapping(value = Messages.ID_MAPPING)
    public ResponseEntity<PublisherDTO> updatePublisher(@RequestBody
                                                        @Valid PublisherDTO publisherDTO,
                                                        @PathVariable(Messages.ID_PATH) Long publisherId) {

        PublisherDTO updatedPublisher = publisherService.updatePublisher(publisherDTO, publisherId);

        return ResponseEntity.status(HttpStatus.OK).body(updatedPublisher);
    }
}
