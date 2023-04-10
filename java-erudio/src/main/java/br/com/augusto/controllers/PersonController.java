package br.com.augusto.controllers;

import br.com.augusto.model.Person;
import br.com.augusto.services.PersonServices;
import br.com.augusto.util.MediaType;
import br.com.augusto.vo.v1.PersonVO;
import br.com.augusto.vo.v2.PersonVOV2;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/person/v1")
@Tag(name = "People", description = "Endpoint for Managing people")
public class PersonController {

    @Autowired
    private PersonServices service;

    @GetMapping(produces ={MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML , MediaType.APPLICATION_YAML})
    @Operation(summary = "Finds all people", description = "Finds all People",
    tags = {"People"},
    responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = {
                    @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation =  PersonVO.class))
                    )
            }),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
    })
    public List<PersonVO> finAll() {
        return  service.findAll();
    }

    @GetMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML })
    @Operation(summary = "Finds a Person", description = "Finds a Person",
            tags = {"People"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = @Content(schema = @Schema(implementation =  PersonVO.class))
                    ),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            })
    public PersonVO finById(@PathVariable(value = "id") Long id) {
        return service.findById(id);
    }


    @PostMapping(consumes =  { MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML },
            produces = { MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML } )
    @Operation(summary = "Add a New Person",
            description = "Add a New Person",
            tags = {"People"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = @Content(schema = @Schema(implementation =  PersonVO.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            })
    public PersonVO create(@RequestBody PersonVO person) {
        return service.create(person);
    }

    @PostMapping(value = "/v2", consumes = { MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML },
            produces =  { MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML } )
    public PersonVOV2 createV2(@RequestBody PersonVOV2 person) {
        return service.createV2(person);
    }


    @PutMapping(consumes =  { MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML },
            produces = { MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML } )
    @Operation(summary = "Updates a Person",
            description = "Updates a Person",
            tags = {"People"},
            responses = {
                    @ApiResponse(description = "Updated", responseCode = "200",
                            content = @Content(schema = @Schema(implementation =  PersonVO.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            })
    public PersonVO update(@RequestBody PersonVO person) {
        return service.update(person);
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Delete a  Person",
            description = "Delete a  Person",
            tags = {"People"},
            responses = {
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            })
    public ResponseEntity<?> delete(@PathVariable(value = "id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
