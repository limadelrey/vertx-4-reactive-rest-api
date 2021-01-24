package org.limadelrey.vertx4.reactive.rest.api.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class BookGetAllResponse implements Serializable {

    private static final long serialVersionUID = -8964658883487451260L;

    @JsonProperty(value = "total")
    private final int total;

    @JsonProperty(value = "limit")
    private final int limit;

    @JsonProperty(value = "page")
    private final int page;

    @JsonProperty(value = "books")
    private final List<BookGetByIdResponse> books;

    public BookGetAllResponse(int total,
                              int limit,
                              int page,
                              List<BookGetByIdResponse> books) {
        this.total = total;
        this.limit = limit;
        this.page = page;
        this.books = books;
    }

    public int getTotal() {
        return total;
    }

    public int getLimit() {
        return limit;
    }

    public int getPage() {
        return page;
    }

    public List<BookGetByIdResponse> getBooks() {
        return books;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookGetAllResponse that = (BookGetAllResponse) o;
        return total == that.total &&
                limit == that.limit &&
                page == that.page &&
                books.equals(that.books);
    }

    @Override
    public int hashCode() {
        return Objects.hash(total, limit, page, books);
    }

    @Override
    public String toString() {
        return "BookGetAllResponse{" +
                "total=" + total +
                ", limit=" + limit +
                ", page=" + page +
                ", books=" + books +
                '}';
    }

}
