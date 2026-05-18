package com.yadot.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckinResponse {
    private Long checkinId;
    private Long habitId;
    private String dataCheckin;
}
