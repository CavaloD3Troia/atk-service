package org.cavalodetroia.atk.domain.sample;

import java.util.Objects;
import java.util.UUID;

/**
 * The placeholder entity of the sample slice. Replace it with the service's real domain.
 */
public record Sample(UUID id, String name) {

    public Sample {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(name, "name");
    }
}
