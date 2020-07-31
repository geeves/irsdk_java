package com.joffrey.irsdkjava.library.yaml.irsdkyaml;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SessionDto {

    @JsonProperty
    private String                     SessionNum              = "";
    @JsonProperty
    private String                     SessionLaps             = "";
    @JsonProperty
    private String                     SessionTime             = "";
    @JsonProperty
    private String                     SessionNumLapsToAvg     = "";
    @JsonProperty
    private String                     SessionType             = "";
    @JsonProperty
    private String                     SessionTrackRubberState = "";
    @JsonProperty
    private List<ResultsPositionsDto>  ResultsPositions;
    @JsonProperty
    private List<ResultsFastestLapDto> ResultsFastestLap;
    @JsonProperty
    private String                     ResultsAverageLapTime   = "";
    @JsonProperty
    private String                     ResultsNumCautionFlags  = "";
    @JsonProperty
    private String                     ResultsNumCautionLaps   = "";
    @JsonProperty
    private String                     ResultsNumLeadChanges   = "";
    @JsonProperty
    private String                     ResultsLapsComplete     = "";
    @JsonProperty
    private String                     ResultsOfficial         = "";
}
