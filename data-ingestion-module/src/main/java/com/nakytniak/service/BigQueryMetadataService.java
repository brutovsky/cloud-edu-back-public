package com.nakytniak.service;

import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.Dataset;
import com.google.cloud.bigquery.Schema;
import com.google.cloud.bigquery.Table;
import com.google.cloud.bigquery.TableId;
import com.nakytniak.dto.FieldSchemaDto;
import com.nakytniak.dto.SchemaListingDto;
import com.nakytniak.dto.TableSchemaDto;
import com.nakytniak.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class BigQueryMetadataService {

    @Value("${gcp.projectId}")
    private String projectId;

    @Value("${gcp.bigquery.datasetId}")
    private String datasetId;

    private final BigQuery bigquery;

    public SchemaListingDto getSchemaListing() {
//        Add caching
        Set<String> tableNames = new HashSet<>();
        final Dataset dataset = bigquery.getDataset(datasetId);

        if (dataset != null) {
            tableNames = dataset.list().streamAll()
                    .map(table -> table.getTableId().getTable())
                    .collect(Collectors.toSet());
        } else {
            log.warn("Dataset not found: [{}]", datasetId);
        }

        return SchemaListingDto.builder().tables(tableNames).build();
    }

    public TableSchemaDto getSchema(final String tableName) {
        final TableId tableId = TableId.of(projectId, datasetId, tableName);
        final Table table = bigquery.getTable(tableId);
        if (table != null) {
            final Schema schema = table.getDefinition().getSchema();
            return mapSchema(tableName, schema);
        } else {
            throw new EntityNotFoundException(tableName);
        }
    }

    private TableSchemaDto mapSchema(final String tableName, final Schema schema) {
        final TableSchemaDto tableSchema = TableSchemaDto.builder().build();
        tableSchema.setTableName(tableName);
        tableSchema.setFields(schema.getFields().stream().map(field -> FieldSchemaDto.builder()
                .name(field.getName())
                .type(field.getType())
                .mode(field.getMode())
                .description(field.getDescription())
                .maxLength(field.getMaxLength())
                .build()).collect(Collectors.toList()));
        return tableSchema;
    }

}
