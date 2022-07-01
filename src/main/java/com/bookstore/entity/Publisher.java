package com.bookstore.entity;

import com.bookstore.controller.dto.PublisherDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "publisher")
public class Publisher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String country;

    public PublisherDTO toDTO() {
        return PublisherDTO.builder()
                .id(id)
                .title(title)
                .country(country).build();
    }
}
