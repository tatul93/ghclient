package com.webfontaine.ghclient.controller;

import com.webfontaine.ghclient.dto.api.BookMarkDto;
import com.webfontaine.ghclient.dto.api.ErrorDto;
import com.webfontaine.ghclient.service.BookMarkService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The controller class for bookmarks
 */
@RestController
@RequestMapping("/bookmarks")
public class BookMarkController {

    private final BookMarkService bookMarkService;

    @Autowired
    public BookMarkController(BookMarkService bookMarkService) {
        this.bookMarkService = bookMarkService;
    }

    /**
     * Add bookmark into db
     *
     * @param cacheId    cache id
     * @param bookMarkId bookmark unique id
     */
    @PostMapping
    @ApiOperation(value = "The method to create a bookmark.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The bookmark successfully created."),
            @ApiResponse(code = 500, message = "The internal error occurred.", response = ErrorDto.class),
            @ApiResponse(code = 409, message = "The duplicated data.", response = ErrorDto.class),
            @ApiResponse(code = 400, message = "Wrong params", response = ErrorDto.class)
    })
    public void add(@RequestParam("cacheId") String cacheId, @RequestParam("bookMarkId") String bookMarkId) {
        bookMarkService.add(cacheId, bookMarkId);
    }

    /**
     * Retrieving all bookmarks
     *
     * @return list of bookmarks
     */
    @GetMapping
    @ApiOperation(value = "The method to get all bookmarks")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The bookmarks successfully retrieved.", response = BookMarkDto.class, responseContainer = "List"),
            @ApiResponse(code = 500, message = "The internal error occurred.", response = ErrorDto.class)
    })
    public List<BookMarkDto> getList() {
        return bookMarkService.findAll();
    }

    /**
     * Get bookmark by id
     *
     * @param id bookmark id
     * @return bookmark
     */
    @GetMapping("/{bookMarkId}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The bookmark successfully retrieved.", response = BookMarkDto.class),
            @ApiResponse(code = 404, message = "The bookmark does not exist.", response = ErrorDto.class),
            @ApiResponse(code = 500, message = "The internal error occurred.", response = ErrorDto.class)
    })
    public BookMarkDto getById(@PathVariable("bookMarkId") String id) {
        return bookMarkService.findById(id);
    }
}
