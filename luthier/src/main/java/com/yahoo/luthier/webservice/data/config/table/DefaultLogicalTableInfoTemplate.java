// Copyright 2018 Yahoo Inc.
// Licensed under the terms of the Apache license. Please see LICENSE.md file distributed with this work for terms.
package com.yahoo.luthier.webservice.data.config.table;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;
import com.yahoo.bard.webservice.data.time.AllGranularity;
import com.yahoo.bard.webservice.data.time.DefaultTimeGrain;
import com.yahoo.bard.webservice.data.time.Granularity;
import com.yahoo.bard.webservice.util.EnumUtils;

import java.util.*;
import java.util.stream.Collectors;


/**
 * Wiki logical table information template.
 * <p>
 * An example:
 * <p>
 *      {
 *          "name": "WIKIPEDIA",
 *          "description": "WIKIPEDIA",
 *          "apiMetricNames": [
 *              "count"
 *          ],
 *          "physicalTables": [
 *              "WIKITICKER"
 *          ],
 *          "granularity": ["ALL", "HOUR", "DAY"]
 *      }
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DefaultLogicalTableInfoTemplate implements LogicalTableInfoTemplate {

    private final String name;
    private final String description;
    private final List<String> apiMetrics;
    private final List<String> physicalTables;
    private final Set<Granularity> granularities;

    /**
     * Constructor used by json parser.
     *
     * @param name           json property name
     * @param description    json property description
     * @param apiMetrics     json property metrics
     * @param physicalTables json property dimensions
     * @param granularities  json property granularities
     */
    @JsonCreator
    public DefaultLogicalTableInfoTemplate(
            @NotNull @JsonProperty("name") String name,
            @JsonProperty("description") String description,
            @JsonProperty("apiMetricNames") List<String> apiMetrics,
            @JsonProperty("physicalTables") List<String> physicalTables,
            @JsonProperty("granularity") List<String> granularities
    ) {
        this.name = EnumUtils.camelCase(name);
        this.description = (Objects.isNull(description) ? "" : description);
        this.apiMetrics = (Objects.isNull(apiMetrics) ?
                Collections.emptyList() : apiMetrics.stream()
                .map(EnumUtils::camelCase)
                .collect(Collectors.toList()));
        this.physicalTables = (Objects.isNull(physicalTables) ?
                Collections.emptyList() : physicalTables.stream()
                .map(EnumUtils::camelCase)
                .collect(Collectors.toList()));
        this.granularities = granularities.stream()
                .map(granularity ->
                        "ALL".equals(granularity) ?
                                AllGranularity.INSTANCE : DefaultTimeGrain.valueOf(granularity))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public List<String> getApiMetrics() {
        return this.apiMetrics;
    }

    @Override
    public List<String> getPhysicalTables() {
        return this.physicalTables;
    }

    @Override
    public Set<Granularity> getGranularities() {
        return this.granularities;
    }
}