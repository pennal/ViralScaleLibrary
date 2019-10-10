package org.viralscale.common.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.*;
import org.viralscale.common.utils.JSONUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DataModel {
    private String id;
    private String origin;
    private Double score;
    private Integer comments;
    private String outboundUrl;
    private LocalDateTime crawlDate;
    private Map<String, Object> additionalData;

    @JsonIgnore
    public String toJson() throws JsonProcessingException {
        return JSONUtils.toJsonString(this);

    }

    @JsonIgnore
    public static DataModel fromJson(byte[] rawData) throws IOException {
        return JSONUtils.fromJson(rawData, DataModel.class);
    }

    @JsonIgnore
    public Long getEpochFromDate() {
        if (this.crawlDate == null) {
            return null;
        }


        return crawlDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

}
