package com.puppypaws.project.dto.Community;

import lombok.Data;

@Data
public class CommunitySearchCondition {
    private String pickupLocation;
    private String status;
    private String dogType;
}
