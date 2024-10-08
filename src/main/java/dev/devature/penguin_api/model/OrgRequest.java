package dev.devature.penguin_api.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrgRequest {
    private String name;
    private Long owner_id;
}
