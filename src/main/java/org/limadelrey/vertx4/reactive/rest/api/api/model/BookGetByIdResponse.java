package org.limadelrey.vertx4.reactive.rest.api.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

public class BookGetByIdResponse implements Serializable {

    private static final long serialVersionUID = 7621071075786169611L;

    @JsonProperty(value = "id")
    private final int id;

    @JsonProperty(value = "author")
    private final String author;

    @JsonProperty(value = "country")
    private final String country;

    @JsonProperty(value = "image_link")
    private final String imageLink;

    @JsonProperty(value = "language")
    private final String language;

    @JsonProperty(value = "link")
    private final String link;

    @JsonProperty(value = "pages")
    private final Integer pages;

    @JsonProperty(value = "title")
    private final String title;

    @JsonProperty(value = "year")
    private final Integer year;

    public BookGetByIdResponse(Book book) {
        this.id = book.getId();
        this.author = book.getAuthor();
        this.country = book.getCountry();
        this.imageLink = book.getImageLink();
        this.language = book.getLanguage();
        this.link = book.getLink();
        this.pages = book.getPages();
        this.title = book.getTitle();
        this.year = book.getYear();
    }

    public int getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getCountry() {
        return country;
    }

    public String getImageLink() {
        return imageLink;
    }

    public String getLanguage() {
        return language;
    }

    public String getLink() {
        return link;
    }

    public Integer getPages() {
        return pages;
    }

    public String getTitle() {
        return title;
    }

    public Integer getYear() {
        return year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookGetByIdResponse that = (BookGetByIdResponse) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "BookGetByIdResponse{" +
                "id=" + id +
                ", author='" + author + '\'' +
                ", country='" + country + '\'' +
                ", imageLink='" + imageLink + '\'' +
                ", language='" + language + '\'' +
                ", link='" + link + '\'' +
                ", pages=" + pages +
                ", title='" + title + '\'' +
                ", year=" + year +
                '}';
    }

}
