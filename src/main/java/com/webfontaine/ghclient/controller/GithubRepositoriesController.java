package com.webfontaine.ghclient.controller;

import com.webfontaine.ghclient.dto.api.ErrorDto;
import com.webfontaine.ghclient.dto.api.SearchResponsesDto;
import com.webfontaine.ghclient.dto.api.StatisticDto;
import com.webfontaine.ghclient.service.GithubService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * The controller class for retrieving data from github repositories
 */
@RestController
@RequestMapping(value = "/repositories")
public class GithubRepositoriesController {

    private final GithubService githubService;

    @Autowired
    public GithubRepositoriesController(GithubService githubService) {
        this.githubService = githubService;
    }

    /**
     * Search repository in github
     *
     * @param query keyword
     * @param start page start
     * @param count page count
     * @return SearchResponsesDto search result
     */
    @GetMapping(value = "/search")
    @ApiOperation(value = "The method to search repositories")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The repositories successfully retrieved.", response = SearchResponsesDto.class),
            @ApiResponse(code = 500, message = "The internal error occurred.", response = ErrorDto.class),
            @ApiResponse(code = 400, message = "Invalid parameter", response = ErrorDto.class)
    })
    public SearchResponsesDto searchRepos(@RequestParam("query") String query,
                                          @RequestParam("start") int start,
                                          @RequestParam("count") int count) {
        return githubService.searchRepositories(query, start, count);
    }

    /**
     * Retrieving commiters list , impact user on project , and commit timeline
     *
     * @param name repository name
     * @return StatisticDto
     */
    @GetMapping(value = "/statistic")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The statistic successfully retrieved.", response = StatisticDto.class),
            @ApiResponse(code = 500, message = "The internal error occurred.", response = ErrorDto.class),
            @ApiResponse(code = 400, message = "Invalid params.", response = ErrorDto.class)
    })
    public StatisticDto getStatistic(@RequestParam(value = "name") String name) {
        return githubService.generateStatisticForRepo(name);
    }
}
