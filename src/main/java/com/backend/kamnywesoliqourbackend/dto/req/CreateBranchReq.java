package com.backend.kamnywesoliqourbackend.dto.req;

import java.util.UUID;

public record CreateBranchReq(
        String name, String location, UUID managerId, Boolean isHq
) {
}
