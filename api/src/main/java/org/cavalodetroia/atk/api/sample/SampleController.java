package org.cavalodetroia.atk.api.sample;

import jakarta.validation.Valid;
import org.cavalodetroia.atk.domain.sample.port.in.CreateSample;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * HTTP only: validate, delegate to the domain, convert the result.
 */
@RestController
@RequestMapping(value = "/api/v1/samples", produces = MediaType.APPLICATION_JSON_VALUE)
public class SampleController {

    private final CreateSample createSample;
    private final SampleConverter converter;

    public SampleController(final CreateSample createSample, final SampleConverter converter) {
        this.createSample = createSample;
        this.converter = converter;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public SampleResponse create(@Valid @RequestBody final CreateSampleRequest request) {
        return converter.toResponse(createSample.create(request.name()));
    }
}
