package com.yadot.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckinRequest {
        private Long habitId;
        private String dataCheckin;
}
