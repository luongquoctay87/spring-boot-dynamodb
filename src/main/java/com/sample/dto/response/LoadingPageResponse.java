package com.sample.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
public class LoadingPageResponse implements Serializable {
    private String nextKey;
    private List<?> items;
}
