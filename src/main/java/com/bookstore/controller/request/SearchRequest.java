package com.bookstore.controller.request;

import lombok.Data;

@Data
public abstract class SearchRequest {

    public Long id;
    public Integer page;
    public Integer limit;
}
